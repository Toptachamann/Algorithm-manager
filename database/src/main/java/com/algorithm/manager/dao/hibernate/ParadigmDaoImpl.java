package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.ParadigmDao;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.DesignParadigm_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class ParadigmDaoImpl extends AbstractDao implements ParadigmDao {

  public ParadigmDaoImpl() {}

  @Override
  public void persist(DesignParadigm paradigm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(paradigm);
    entityManager.getTransaction().commit();
  }

  @Override
  public List<DesignParadigm> getAllParadigms() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    List<DesignParadigm> paradigms =
        entityManager
            .createQuery(criteria.select(criteria.from(DesignParadigm.class)))
            .getResultList();
    return paradigms;
  }

  @Override
  public Optional<DesignParadigm> getParadigmById(int id) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    Root<DesignParadigm> root = criteria.from(DesignParadigm.class);
    List<DesignParadigm> paradigms =
        entityManager
            .createQuery(
                criteria.select(root).where(builder.equal(root.get(DesignParadigm_.id), id)))
            .getResultList();
    return paradigms.size() == 1 ? Optional.of(paradigms.get(0)) : Optional.empty();
  }

  @Override
  public Optional<DesignParadigm> getParadigmByParadigm(String paradigm) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    Root<DesignParadigm> root = criteria.from(DesignParadigm.class);
    List<DesignParadigm> paradigms =
        entityManager
            .createQuery(
                criteria
                    .select(root)
                    .where(builder.equal(root.get(DesignParadigm_.paradigm), paradigm)))
            .getResultList();
    return paradigms.size() == 1 ? Optional.of(paradigms.get(0)) : Optional.empty();
  }

  @Override
  public void delete(DesignParadigm paradigm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(entityManager.contains(paradigm) ? paradigm : entityManager.merge(paradigm));
    entityManager.getTransaction().commit();
  }

  @Override
  public void merge(DesignParadigm paradigm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.merge(paradigm);
    entityManager.getTransaction().commit();
  }
}
