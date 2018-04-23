package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.AuthorDao;
import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Author_;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl extends AbstractDao implements AuthorDao {

  @Override
  public int persist(Author author) {
    Session session = getSession();
    session.getTransaction().begin();
    session.saveOrUpdate(author);
    session.getTransaction().commit();
    return author.getId();
  }

  @Override
  public void merge(Author author) {
    Session session = getSession();
    session.getTransaction().begin();
    session.merge(author);
    session.getTransaction().commit();
  }

  @Override
  public Optional<Author> getById(int id) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Author> query = builder.createQuery(Author.class);
    Root<Author> root = query.from(Author.class);
    query.where(builder.equal(root.get(Author_.id), id));
    List<Author> result = session.createQuery(query).getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public Optional<Author> getByFullName(String firstName, String lastName) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Author> query = builder.createQuery(Author.class);
    Root<Author> root = query.from(Author.class);
    query.where(
        builder.and(
            builder.equal(root.get(Author_.firstName), firstName),
            builder.equal(root.get(Author_.lastName), lastName)));
    List<Author> result = session.createQuery(query).getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public void deleteById(int id) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Author> delete = builder.createCriteriaDelete(Author.class);
    Root<Author> root = delete.from(Author.class);
    delete.where(builder.equal(root.get(Author_.id), id));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  public void delete(Author author) {
    Session session = getSession();
    session.getTransaction().begin();
    session.remove(session.contains(author) ? author : session.merge(author));
    session.getTransaction().commit();
  }

  @Override
  public void deleteByFullName(String firstName, String lastName) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Author> delete = builder.createCriteriaDelete(Author.class);
    Root<Author> root = delete.from(Author.class);
    delete.where(
        builder.and(
            builder.equal(root.get(Author_.firstName), firstName),
            builder.equal(root.get(Author_.lastName), lastName)));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }
}
