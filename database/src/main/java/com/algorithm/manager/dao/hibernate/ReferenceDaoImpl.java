package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.ReferenceDao;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.model.Reference;
import com.algorithm.manager.model.Reference_;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ReferenceDaoImpl extends AbstractDao implements ReferenceDao {

  @Override
  public int persist(Reference reference) {
    Session session = getSession();
    session.getTransaction().begin();
    session.persist(reference);
    session.getTransaction().commit();
    return reference.getId();
  }

  @Override
  public List<Reference> getReferences(Algorithm algorithm, Book book) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Reference> query = builder.createQuery(Reference.class);
    Root<Reference> root = query.from(Reference.class);
    query.where(
        builder.and(
            builder.equal(root.get(Reference_.algorithm), algorithm),
            builder.equal(root.get(Reference_.book), book)));
    return session.createQuery(query).getResultList();
  }

  @Override
  public List<Reference> getAlgorithmReferences(Algorithm algorithm) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Reference> query = builder.createQuery(Reference.class);
    Root<Reference> root = query.from(Reference.class);
    query.where(builder.equal(root.get(Reference_.algorithm), algorithm));
    return session.createQuery(query).getResultList();
  }

  @Override
  public List<Reference> getReferencesInBook(Book book) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Reference> query = builder.createQuery(Reference.class);
    Root<Reference> root = query.from(Reference.class);
    query.where(builder.equal(root.get(Reference_.book), book));
    return session.createQuery(query).getResultList();
  }

  @Override
  public void delete(Reference reference) {
    Session session = getSession();
    session.getTransaction().begin();
    session.remove(session.contains(reference) ? reference : session.merge(reference));
    session.getTransaction().commit();
  }

  @Override
  public void deleteById(int id) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Reference> delete = builder.createCriteriaDelete(Reference.class);
    Root<Reference> root = delete.from(Reference.class);
    delete.where(builder.equal(root.get(Reference_.id), id));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void deleteReferences(Algorithm algorithm, Book book) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Reference> delete = builder.createCriteriaDelete(Reference.class);
    Root<Reference> root = delete.from(Reference.class);
    delete.where(
        builder.and(builder.equal(root.get(Reference_.algorithm), algorithm)),
        builder.equal(root.get(Reference_.book), book));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }
}
