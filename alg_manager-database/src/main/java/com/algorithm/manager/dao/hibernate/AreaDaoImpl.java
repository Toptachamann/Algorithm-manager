package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.AreaDao;
import com.algorithm.manager.model.AreaOfUse;
import com.algorithm.manager.model.AreaOfUse_;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class AreaDaoImpl extends AbstractDao implements AreaDao {

  @Override
  public int persist(AreaOfUse areaOfUse) {
    Session session = getSession();
    session.getTransaction().begin();
    session.persist(areaOfUse);
    session.getTransaction().commit();
    return areaOfUse.getId();
  }

  @Override
  public List<AreaOfUse> getAllAreas() {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    query.from(AreaOfUse.class);
    return session.createQuery(query).getResultList();
  }

  @Override
  public Optional<AreaOfUse> getAreaOfUseById(int id) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    Root<AreaOfUse> root = query.from(AreaOfUse.class);
    List<AreaOfUse> result =
        session
            .createQuery(query.where(builder.equal(root.get(AreaOfUse_.id), id)))
            .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public Optional<AreaOfUse> getAreaOfUseByName(String name) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    Root<AreaOfUse> root = query.from(AreaOfUse.class);
    List<AreaOfUse> result =
        session
            .createQuery(query.where(builder.equal(root.get(AreaOfUse_.name), name)))
            .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public void delete(AreaOfUse areaOfUse) {
    Session session = getSession();
    session.getTransaction().begin();
    session.remove(session.contains(areaOfUse) ? areaOfUse : session.merge(areaOfUse));
    session.getTransaction().commit();
  }

  @Override
  public void deleteById(int id) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<AreaOfUse> delete = builder.createCriteriaDelete(AreaOfUse.class);
    Root<AreaOfUse> root = delete.from(AreaOfUse.class);
    delete.where(builder.equal(root.get(AreaOfUse_.id), id));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void deleteByName(String name) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<AreaOfUse> delete = builder.createCriteriaDelete(AreaOfUse.class);
    Root<AreaOfUse> root = delete.from(AreaOfUse.class);
    delete.where(builder.equal(root.get(AreaOfUse_.name), name));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }
}
