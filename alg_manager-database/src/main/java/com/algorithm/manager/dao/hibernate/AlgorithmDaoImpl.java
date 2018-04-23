package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.dao.interf.AlgorithmDao;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Algorithm_;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlgorithmDaoImpl extends AbstractDao implements AlgorithmDao {

  @Override
  public int persist(Algorithm algorithm) {
    Session session = getSession();
    session.getTransaction().begin();
    session.saveOrUpdate(algorithm);
    session.getTransaction().commit();
    return algorithm.getId();
  }

  @Override
  public List<Algorithm> getAlgorithms() {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Algorithm> query = builder.createQuery(Algorithm.class);
    query.from(Algorithm.class);
    return session.createQuery(query).getResultList();
  }

  @Override
  public Optional<Algorithm> getAlgorithmById(int id) {
    Session session = getSession();
    Algorithm algorithm = session.get(Algorithm.class, id);
    return algorithm == null ? Optional.empty() : Optional.of(algorithm);
  }

  @Override
  public Optional<Algorithm> getAlgorithmByName(String name) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Algorithm> query = builder.createQuery(Algorithm.class);
    Root<Algorithm> root = query.from(Algorithm.class);
    List<Algorithm> result =
        session
            .createQuery(query.where(builder.equal(root.get(Algorithm_.name), name)))
            .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public List<Algorithm> searchAlgorithm(
      String algorithm,
      String complexity,
      DesignParadigm designParadigm,
      FieldOfStudy fieldOfStudy) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Algorithm> query = builder.createQuery(Algorithm.class);
    Root<Algorithm> root = query.from(Algorithm.class);
    List<Predicate> predicates = new ArrayList<>();
    if (!StringUtils.isBlank(algorithm)) {
      predicates.add(builder.like(root.get(Algorithm_.name), Util.fixForLike(algorithm)));
    }
    if (!StringUtils.isBlank(complexity)) {
      predicates.add(builder.like(root.get(Algorithm_.complexity), Util.fixForLike(complexity)));
    }
    if (designParadigm != null) {
      predicates.add(builder.equal(root.get(Algorithm_.designParadigm), designParadigm));
    }
    if (fieldOfStudy != null) {
      predicates.add(builder.equal(root.get(Algorithm_.fieldOfStudy), fieldOfStudy));
    }
    query.where(predicates.toArray(new Predicate[] {}));
    return session.createQuery(query).getResultList();
  }

  @Override
  public void merge(Algorithm algorithm) {
    Session session = getSession();
    session.getTransaction().begin();
    session.merge(algorithm);
    session.getTransaction().commit();
  }

  @Override
  public void setName(int id, String name) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<Algorithm> update = builder.createCriteriaUpdate(Algorithm.class);
    Root<Algorithm> root = update.from(Algorithm.class);
    update.set(root.get(Algorithm_.name), name);
    update.where(builder.equal(root.get(Algorithm_.id), id));
    session.createQuery(update).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void setComplexity(int id, String complexity) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<Algorithm> update = builder.createCriteriaUpdate(Algorithm.class);
    Root<Algorithm> root = update.from(Algorithm.class);
    update.set(root.get(Algorithm_.complexity), complexity);
    update.where(builder.equal(root.get(Algorithm_.id), id));
    session.createQuery(update).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void setDesignParadigm(int id, DesignParadigm designParadigm) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<Algorithm> update = builder.createCriteriaUpdate(Algorithm.class);
    Root<Algorithm> root = update.from(Algorithm.class);
    update.set(root.get(Algorithm_.designParadigm), designParadigm);
    update.where(builder.equal(root.get(Algorithm_.id), id));
    session.createQuery(update).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void setFieldOfStudy(int id, FieldOfStudy fieldOfStudy) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<Algorithm> update = builder.createCriteriaUpdate(Algorithm.class);
    Root<Algorithm> root = update.from(Algorithm.class);
    update.set(root.get(Algorithm_.fieldOfStudy), fieldOfStudy);
    update.where(builder.equal(root.get(Algorithm_.id), id));
    session.createQuery(update).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void delete(Algorithm algorithm) {
    Session session = getSession();
    session.getTransaction().begin();
    session.remove(session.contains(algorithm) ? algorithm : session.merge(algorithm));
    session.getTransaction().commit();
  }

  @Override
  public void deleteById(int id) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Algorithm> delete = builder.createCriteriaDelete(Algorithm.class);
    Root<Algorithm> root = delete.from(Algorithm.class);
    delete.where(builder.equal(root.get(Algorithm_.id), id));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void deleteByName(String name) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Algorithm> delete = builder.createCriteriaDelete(Algorithm.class);
    Root<Algorithm> root = delete.from(Algorithm.class);
    delete.where(builder.equal(root.get(Algorithm_.name), name));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }
}
