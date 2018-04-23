package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.BookDao;
import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.model.Book_;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

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
  public int persist(Book book) {
    Session session = getSession();
    session.getTransaction().begin();
    session.persist(book);
    session.getTransaction().commit();
    return book.getId();
  }

  @Override
  public List<Book> getBooks() {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Book> query = builder.createQuery(Book.class);
    query.from(Book.class);
    return session.createQuery(query).getResultList();
  }

  @Override
  public List<Book> searchBooks(
      String title, Integer volume, Integer edition, List<Author> authors) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
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
    return session.createQuery(query).getResultList();
  }

  @Override
  public Optional<Book> getBookById(int id) {
    Session session = getSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Book> query = builder.createQuery(Book.class);
    Root<Book> root = query.from(Book.class);
    query.where(builder.equal(root.get(Book_.id), id));
    List<Book> books = session.createQuery(query).getResultList();
    return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
  }

  @Override
  public void merge(Book book) {
    Session session = getSession();
    session.getTransaction().begin();
    session.merge(book);
    session.getTransaction().commit();
  }

  @Override
  public void delete(Book book) {
    Session session = getSession();
    session.getTransaction().begin();
    session.remove(session.contains(book) ? book : session.merge(book));
    session.getTransaction().commit();
  }

  @Override
  public void deleteById(int id) {
    Session session = getSession();
    session.getTransaction().begin();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaDelete<Book> delete = builder.createCriteriaDelete(Book.class);
    Root<Book> root = delete.from(Book.class);
    delete.where(builder.equal(root.get(Book_.title), id));
    session.createQuery(delete).executeUpdate();
    session.getTransaction().commit();
  }
}
