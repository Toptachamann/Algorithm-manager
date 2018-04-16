package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.dao.interf.AlgorithmDao;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Algorithm_;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlgorithmDaoImpl extends AbstractDao implements AlgorithmDao {
  public AlgorithmDaoImpl() {}

  @Override
  public void persist(Algorithm algorithm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(algorithm);
    entityManager.getTransaction().commit();
  }

  @Override
  public List<Algorithm> getAllAlgorithms() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Algorithm> query = builder.createQuery(Algorithm.class);
    query.from(Algorithm.class);
    return entityManager.createQuery(query).getResultList();
  }

  @Override
  public Optional<Algorithm> getAlgorithmByName(String name) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Algorithm> query = builder.createQuery(Algorithm.class);
    Root<Algorithm> root = query.from(Algorithm.class);
    List<Algorithm> result =
        entityManager
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
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
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
    return entityManager.createQuery(query).getResultList();
  }

  @Override
  public void merge(Algorithm algorithm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.merge(algorithm);
    entityManager.getTransaction().commit();
  }

  @Override
  public void deleteById(Algorithm algorithm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(
        entityManager.contains(algorithm) ? algorithm : entityManager.merge(algorithm));
    entityManager.getTransaction().commit();
  }

  @Override
  public void deleteByName(Algorithm algorithm) throws Exception {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<Algorithm> delete = builder.createCriteriaDelete(Algorithm.class);
    Root<Algorithm> root = delete.from(Algorithm.class);
    delete.where(builder.equal(root.get(Algorithm_.name), algorithm.getName()));
    entityManager.createQuery(delete).executeUpdate();
    entityManager.getTransaction().commit();
  }
}
