package app.dao.hibernate;

import app.auxiliary.Util;
import app.dao.interf.AlgorithmDao;
import app.model.Algorithm;
import app.model.Algorithm_;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlgorithmDaoImpl extends AbstractDao implements AlgorithmDao {
  @Override
  public void persist(Algorithm algorithm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(algorithm);
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public List<Algorithm> getAllAlgorithms() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Algorithm> query = builder.createQuery(Algorithm.class);
    query.from(Algorithm.class);
    List<Algorithm> result = entityManager.createQuery(query).getResultList();
    entityManager.close();
    return result;
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
    entityManager.close();
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
    List<Algorithm> result = entityManager.createQuery(query).getResultList();
    entityManager.close();
    return result;
  }

  @Override
  public void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Algorithm> update = builder.createCriteriaUpdate(Algorithm.class);
    Root<Algorithm> root = update.from(Algorithm.class);
    update.set(root.get(Algorithm_.designParadigm), paradigm);
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void setFieldOfStudy(Algorithm algorithm, FieldOfStudy fieldOfStudy) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Algorithm> update = builder.createCriteriaUpdate(Algorithm.class);
    Root<Algorithm> root = update.from(Algorithm.class);
    update.set(root.get(Algorithm_.fieldOfStudy), fieldOfStudy);
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void setName(Algorithm algorithm, String name) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Algorithm> update = builder.createCriteriaUpdate(Algorithm.class);
    Root<Algorithm> root = update.from(Algorithm.class);
    update.set(root.get(Algorithm_.name), name);
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void setComplexity(Algorithm algorithm, String complexity) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Algorithm> update = builder.createCriteriaUpdate(Algorithm.class);
    Root<Algorithm> root = update.from(Algorithm.class);
    update.set(root.get(Algorithm_.complexity), complexity);
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void delete(Algorithm algorithm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(
        entityManager.contains(algorithm) ? algorithm : entityManager.merge(algorithm));
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
