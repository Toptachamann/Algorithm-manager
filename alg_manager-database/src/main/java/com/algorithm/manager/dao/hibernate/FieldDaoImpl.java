package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.FieldDao;
import com.algorithm.manager.model.FieldOfStudy;
import com.algorithm.manager.model.FieldOfStudy_;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class FieldDaoImpl extends AbstractDao implements FieldDao {

  @Override
  public int persist(FieldOfStudy fieldOfStudy) {
    Session session = getSession();
    session.getTransaction().begin();
    session.saveOrUpdate(fieldOfStudy);
    session.getTransaction().commit();
    return fieldOfStudy.getId();
  }

  @Override
  public FieldOfStudy persist(String name, @Nullable String description) {
    FieldOfStudy fieldOfStudy = new FieldOfStudy(name, description);
    persist(fieldOfStudy);
    return fieldOfStudy;
  }

  @Override
  public List<FieldOfStudy> getFieldsOfStudy() {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteriaQuery = builder.createQuery(FieldOfStudy.class);
    return session
        .createQuery(criteriaQuery.select(criteriaQuery.from(FieldOfStudy.class)))
        .getResultList();
  }

  @Override
  public Optional<FieldOfStudy> getFieldOfStudyByName(String name) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteria = builder.createQuery(FieldOfStudy.class);
    Root<FieldOfStudy> root = criteria.from(FieldOfStudy.class);
    List<FieldOfStudy> fields =
        session
            .createQuery(
                criteria.select(root).where(builder.equal(root.get(FieldOfStudy_.name), name)))
            .getResultList();
    return fields.size() == 1 ? Optional.of(fields.get(0)) : Optional.empty();
  }

  @Override
  public Optional<FieldOfStudy> getFieldOfStudyById(int id) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteria = builder.createQuery(FieldOfStudy.class);
    Root<FieldOfStudy> root = criteria.from(FieldOfStudy.class);
    List<FieldOfStudy> fields =
        session
            .createQuery(criteria.select(root).where(builder.equal(root.get(FieldOfStudy_.id), id)))
            .getResultList();
    return fields.size() == 1 ? Optional.of(fields.get(0)) : Optional.empty();
  }

  @Override
  public void merge(FieldOfStudy fieldOfStudy) {
    Session session = getSession();
    session.getTransaction().begin();
    session.merge(fieldOfStudy);
    session.getTransaction().commit();
  }

  @Override
  public void setName(int id, String name) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<FieldOfStudy> update = builder.createCriteriaUpdate(FieldOfStudy.class);
    Root<FieldOfStudy> root = update.from(FieldOfStudy.class);
    update
        .where(builder.equal(root.get(FieldOfStudy_.id), id))
        .set(root.get(FieldOfStudy_.name), name);
    session.createQuery(update).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void setDescription(int id, String description) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<FieldOfStudy> update = builder.createCriteriaUpdate(FieldOfStudy.class);
    Root<FieldOfStudy> root = update.from(FieldOfStudy.class);
    update
        .where(builder.equal(root.get(FieldOfStudy_.id), id))
        .set(root.get(FieldOfStudy_.description), description);
    session.createQuery(update).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void delete(FieldOfStudy fieldOfStudy) {
    Session session = getSession();
    session.getTransaction().begin();
    session.remove(session.contains(fieldOfStudy) ? fieldOfStudy : session.merge(fieldOfStudy));
    session.getTransaction().commit();
  }

  @Override
  public void deleteByName(String name) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<FieldOfStudy> delete = builder.createCriteriaDelete(FieldOfStudy.class);
    Root<FieldOfStudy> root = delete.from(FieldOfStudy.class);
    delete.where(builder.equal(root.get(FieldOfStudy_.name), name));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void deleteById(int id) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<FieldOfStudy> delete = builder.createCriteriaDelete(FieldOfStudy.class);
    Root<FieldOfStudy> root = delete.from(FieldOfStudy.class);
    delete.where(builder.equal(root.get(FieldOfStudy_.id), id));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }
}
