package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.AreaDao;
import com.algorithm.manager.model.AreaOfUse;
import com.algorithm.manager.model.AreaOfUse_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class AreaDaoImpl extends AbstractDao implements AreaDao {
  public AreaDaoImpl() {}

  @Override
  public List<AreaOfUse> getAllAreas() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    query.from(AreaOfUse.class);
    List<AreaOfUse> areas = entityManager.createQuery(query).getResultList();
    return areas;
  }

  @Override
  public void persist(AreaOfUse areaOfUse) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(areaOfUse);
    entityManager.getTransaction().commit();
  }

  @Override
  public Optional<AreaOfUse> getAreaById(int id) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    Root<AreaOfUse> root = query.from(AreaOfUse.class);
    List<AreaOfUse> result =
        entityManager
            .createQuery(query.where(builder.equal(root.get(AreaOfUse_.id), id)))
            .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public Optional<AreaOfUse> getAreaByName(String name) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    Root<AreaOfUse> root = query.from(AreaOfUse.class);
    List<AreaOfUse> result =
        entityManager
            .createQuery(query.where(builder.equal(root.get(AreaOfUse_.areaOfUse), name)))
            .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public void deleteById(AreaOfUse areaOfUse) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(
        entityManager.contains(areaOfUse) ? areaOfUse : entityManager.merge(areaOfUse));
    entityManager.getTransaction().commit();
  }

  @Override
  public void deleteByArea(AreaOfUse areaOfUse) throws Exception {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<AreaOfUse> delete = builder.createCriteriaDelete(AreaOfUse.class);
    Root<AreaOfUse> root = delete.from(AreaOfUse.class);
    delete.where(builder.equal(root.get(AreaOfUse_.areaOfUse), areaOfUse.getAreaOfUse()));
    entityManager.createQuery(delete).executeUpdate();
    entityManager.getTransaction().commit();
  }
}
