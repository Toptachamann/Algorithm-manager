package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.BookDao;
import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.model.Book_;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl extends AbstractDao implements BookDao {
  public BookDaoImpl() {}

  @Override
  public void persist(Book book) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(book);
    entityManager.getTransaction().commit();
  }

  @Override
  public List<Book> getBooks() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Book> query = builder.createQuery(Book.class);
    query.from(Book.class);
    List<Book> books = entityManager.createQuery(query).getResultList();
    return books;
  }

  @Override
  public List<Book> searchBooks(
      String title, Integer volume, Integer edition, List<Author> authors) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Book> query = builder.createQuery(Book.class);
    Root<Book> root = query.from(Book.class);
    List<Predicate> predicates = new ArrayList<>();
    if (!StringUtils.isBlank(title)) {
      predicates.add(builder.equal(root.get(Book_.title), title));
    }
    if (volume != null) {
      predicates.add(builder.equal(root.get(Book_.volume), volume));
    }
    if (edition != null) {
      predicates.add(builder.equal(root.get(Book_.edition), edition));
    }
    if (authors != null && !authors.isEmpty()) {
      // TODO: fix
    }
    query.where(predicates.toArray(new Predicate[] {}));
    List<Book> books = entityManager.createQuery(query).getResultList();
    return books;
  }

  @Override
  public Optional<Book> getBookById(int id) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Book> query = builder.createQuery(Book.class);
    Root<Book> root = query.from(Book.class);
    query.where(builder.equal(root.get(Book_.id), id));
    List<Book> books = entityManager.createQuery(query).getResultList();
    return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
  }

  @Override
  public void merge(Book book) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.merge(book);
    entityManager.getTransaction().commit();
  }

  @Override
  public void delete(Book book) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(entityManager.contains(book) ? book : entityManager.merge(book));
    entityManager.getTransaction().commit();
  }

  @Override
  public void deleteByTitle(Book book) throws Exception {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<Book> delete = builder.createCriteriaDelete(Book.class);
    Root<Book> root = delete.from(Book.class);
    delete.where(builder.equal(root.get(Book_.title), book.getTitle()));
    entityManager.createQuery(delete).executeUpdate();
    entityManager.getTransaction().commit();
  }
}
