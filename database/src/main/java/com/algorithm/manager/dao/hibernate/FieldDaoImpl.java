package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.FieldDao;
import com.algorithm.manager.model.FieldOfStudy;
import com.algorithm.manager.model.FieldOfStudy_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class FieldDaoImpl extends AbstractDao implements FieldDao {

  public FieldDaoImpl() {}

  @Override
  public void persist(FieldOfStudy fieldOfStudy) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(fieldOfStudy);
    entityManager.getTransaction().commit();
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteriaQuery = builder.createQuery(FieldOfStudy.class);
    return entityManager
        .createQuery(criteriaQuery.select(criteriaQuery.from(FieldOfStudy.class)))
        .getResultList();
  }

  @Override
  public Optional<FieldOfStudy> getFieldByName(String name) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteria = builder.createQuery(FieldOfStudy.class);
    Root<FieldOfStudy> root = criteria.from(FieldOfStudy.class);
    List<FieldOfStudy> fields =
        entityManager
            .createQuery(
                criteria.select(root).where(builder.equal(root.get(FieldOfStudy_.field), name)))
            .getResultList();
    return fields.size() == 1 ? Optional.of(fields.get(0)) : Optional.empty();
  }

  @Override
  public Optional<FieldOfStudy> getFieldById(int id) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteria = builder.createQuery(FieldOfStudy.class);
    Root<FieldOfStudy> root = criteria.from(FieldOfStudy.class);
    List<FieldOfStudy> fields =
        entityManager
            .createQuery(criteria.select(root).where(builder.equal(root.get(FieldOfStudy_.id), id)))
            .getResultList();
    return fields.size() == 1 ? Optional.of(fields.get(0)) : Optional.empty();
  }

  @Override
  public void merge(FieldOfStudy fieldOfStudy) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.merge(fieldOfStudy);
    entityManager.getTransaction().commit();
  }

  @Override
  public void delete(FieldOfStudy fieldOfStudy) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(
        entityManager.contains(fieldOfStudy) ? fieldOfStudy : entityManager.merge(fieldOfStudy));
    entityManager.getTransaction().commit();
  }
}
