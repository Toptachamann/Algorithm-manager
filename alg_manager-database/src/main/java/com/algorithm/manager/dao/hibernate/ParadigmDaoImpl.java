package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.ParadigmDao;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.DesignParadigm_;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class ParadigmDaoImpl extends AbstractDao implements ParadigmDao {

  @Override
  public int persist(DesignParadigm paradigm) {
    Session session = getSession();
    session.getTransaction().begin();
    session.saveOrUpdate(paradigm);
    session.getTransaction().commit();
    return paradigm.getDesignParadigmId();
  }

  @Override
  public void refresh(DesignParadigm paradigm) {
    Session session = getSession();
    session.refresh(paradigm);
  }

  @Override
  public List<DesignParadigm> getAllParadigms() {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    return session
        .createQuery(criteria.select(criteria.from(DesignParadigm.class)))
        .getResultList();
  }

  @Override
  public Optional<DesignParadigm> getParadigmById(int id) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    Root<DesignParadigm> root = criteria.from(DesignParadigm.class);
    List<DesignParadigm> paradigms =
        session
            .createQuery(
                criteria.select(root).where(builder.equal(root.get(DesignParadigm_.designParadigmId), id)))
            .getResultList();
    return paradigms.size() == 1 ? Optional.of(paradigms.get(0)) : Optional.empty();
  }

  @Override
  public Optional<DesignParadigm> getDesignParadigmByName(String name) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    Root<DesignParadigm> root = criteria.from(DesignParadigm.class);
    List<DesignParadigm> paradigms =
        session
            .createQuery(
                criteria.select(root).where(builder.equal(root.get(DesignParadigm_.name), name)))
            .getResultList();
    return paradigms.size() == 1 ? Optional.of(paradigms.get(0)) : Optional.empty();
  }

  @Override
  public void merge(DesignParadigm paradigm) {
    Session session = getSession();
    session.getTransaction().begin();
    session.merge(paradigm);
    session.getTransaction().commit();
  }

  @Override
  public void setName(int id, String name) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<DesignParadigm> update = builder.createCriteriaUpdate(DesignParadigm.class);
    Root<DesignParadigm> root = update.from(DesignParadigm.class);
    update
        .where(builder.equal(root.get(DesignParadigm_.designParadigmId), id))
        .set(root.get(DesignParadigm_.name), name);
    session.createQuery(update).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void setDescription(int id, @Nullable String description) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaUpdate<DesignParadigm> update = builder.createCriteriaUpdate(DesignParadigm.class);
    Root<DesignParadigm> root = update.from(DesignParadigm.class);
    update
        .where(builder.equal(root.get(DesignParadigm_.designParadigmId), id))
        .set(root.get(DesignParadigm_.description), description);
    session.createQuery(update).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void delete(DesignParadigm paradigm) {
    Session session = getSession();
    session.getTransaction().begin();
    session.remove(session.contains(paradigm) ? paradigm : session.merge(paradigm));
    session.getTransaction().commit();
  }

  @Override
  public int deleteByName(String name) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<DesignParadigm> delete = builder.createCriteriaDelete(DesignParadigm.class);
    Root<DesignParadigm> root = delete.from(DesignParadigm.class);
    delete.where(builder.equal(root.get(DesignParadigm_.name), name));
    int rowNumber = session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
    return rowNumber;
  }

  @Override
  public int deleteById(int id) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<DesignParadigm> delete = builder.createCriteriaDelete(DesignParadigm.class);
    Root<DesignParadigm> root = delete.from(DesignParadigm.class);
    delete.where(builder.equal(root.get(DesignParadigm_.designParadigmId), id));
    int rowNumber = session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
    return rowNumber;
  }
}
