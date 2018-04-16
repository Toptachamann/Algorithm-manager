package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.AuthorDao;
import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Author_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl extends AbstractDao implements AuthorDao {

  public AuthorDaoImpl() {}

  @Override
  public void persist(Author author) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(author);
    entityManager.getTransaction().commit();
  }

  @Override
  public void deleteById(Author author) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(entityManager.contains(author) ? author : entityManager.merge(author));
    entityManager.getTransaction().commit();
  }

  @Override
  public void deleteByFullName(Author author) throws Exception {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<Author> delete = builder.createCriteriaDelete(Author.class);
    Root<Author> root = delete.from(Author.class);
    delete.where(
        builder.and(
            builder.equal(root.get(Author_.firstName), author.getFirstName()),
            builder.equal(root.get(Author_.lastName), author.getLastName())));
    entityManager.createQuery(delete).executeUpdate();
    entityManager.getTransaction().commit();
  }

  @Override
  public Optional<Author> getAuthor(String firstName, String lastName) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Author> query = builder.createQuery(Author.class);
    Root<Author> root = query.from(Author.class);
    query.where(
        builder.and(
            builder.equal(root.get(Author_.firstName), firstName),
            builder.equal(root.get(Author_.lastName), lastName)));
    List<Author> result = entityManager.createQuery(query).getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }
}
