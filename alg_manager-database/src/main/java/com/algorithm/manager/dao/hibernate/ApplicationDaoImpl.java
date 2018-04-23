package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.ApplicationDao;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Application;
import com.algorithm.manager.model.Application_;
import com.algorithm.manager.model.AreaOfUse;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ApplicationDaoImpl extends AbstractDao implements ApplicationDao {

  @Override
  public int persist(Application application) {
    Session session = getSession();
    session.getTransaction().begin();
    session.saveOrUpdate(application);
    session.getTransaction().commit();
    return application.getId();
  }

  @Override
  public List<Application> getApplicationsOf(Algorithm algorithm, AreaOfUse areaOfUse) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Application> query = builder.createQuery(Application.class);
    Root<Application> root = query.from(Application.class);
    query.where(
        builder.and(
            builder.equal(root.get(Application_.algorithm), algorithm),
            builder.equal(root.get(Application_.areaOfUse), areaOfUse)));
    return session.createQuery(query).getResultList();
  }

  @Override
  public List<Application> getApplicationsByArea(AreaOfUse area) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Application> query = builder.createQuery(Application.class);
    Root<Application> root = query.from(Application.class);
    query.where(builder.equal(root.get(Application_.areaOfUse), area));
    return session.createQuery(query).getResultList();
  }

  @Override
  public List<Application> getApplicationsByAlgorithm(Algorithm algorithm) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Application> query = builder.createQuery(Application.class);
    Root<Application> root = query.from(Application.class);
    query.where(builder.equal(root.get(Application_.algorithm), algorithm));
    return session.createQuery(query).getResultList();
  }

  @Override
  public void delete(Application application) {
    Session session = getSession();
    session.getTransaction().begin();
    session.remove(session.contains(application) ? application : session.merge(application));
    session.getTransaction().commit();
  }

  @Override
  public void deleteApplications(Algorithm algorithm, AreaOfUse areaOfUse) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Application> delete = builder.createCriteriaDelete(Application.class);
    Root<Application> root = delete.from(Application.class);
    delete.where(
        builder.and(builder.equal(root.get(Application_.algorithm), algorithm)),
        builder.equal(root.get(Application_.areaOfUse), areaOfUse));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void deleteById(int id) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Application> delete = builder.createCriteriaDelete(Application.class);
    Root<Application> root = delete.from(Application.class);
    delete.where(builder.equal(root.get(Application_.id), id));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }
}
