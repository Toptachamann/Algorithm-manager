package app.dao.hibernate;

import app.dao.interf.BookDao;
import app.model.Author;
import app.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl extends AbstractDao implements BookDao {
  @Override
  public Book createBook(
      String title, @Nullable Integer volume, Integer edition, List<Author> authors) {
    Book book = new Book(title, volume, edition, authors);
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(book);
    entityManager.getTransaction().commit();
    entityManager.close();
    return book;
  }

  @Override
  public List<Book> getBooks() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Book> query = builder.createQuery(Book.class);
    query.from(Book.class);
    List<Book> books = entityManager.createQuery(query).getResultList();
    entityManager.close();
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
    entityManager.close();
    return books;
  }

  @Override
  public void setTitle(Book book, String title) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Book> update = builder.createCriteriaUpdate(Book.class);
    Root<Book> root = update.from(Book.class);
    update.set(root.get(Book_.title), title);
    update.where(builder.equal(root.get(Book_.id), book.getId()));
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void setVolume(Book book, Integer volume) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Book> update = builder.createCriteriaUpdate(Book.class);
    Root<Book> root = update.from(Book.class);
    update.set(root.get(Book_.volume), volume);
    update.where(builder.equal(root.get(Book_.id), book.getId()));
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void setEdition(Book book, int edition) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Book> update = builder.createCriteriaUpdate(Book.class);
    Root<Book> root = update.from(Book.class);
    update.set(root.get(Book_.edition), edition);
    update.where(builder.equal(root.get(Book_.id), book.getId()));
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void deleteBook(Book book) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<Book> query = builder.createCriteriaDelete(Book.class);
    Root<Book> root = query.from(Book.class);
    query.where(builder.equal(root.get(Book_.id), book.getId()));
    entityManager.createQuery(query).executeUpdate();
    entityManager.close();
  }

  @Override
  public void addAuthors(Book book, List<Author> authors) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Book> update = builder.createCriteriaUpdate(Book.class);
    Root<Book> root = update.from(Book.class);
    update.set(root.get(Book_.book), book.getAuthors().addAll(authors))
    update.where(builder.equal(root.get(Book_.id), book.getId()));
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
  }

  @Override
  public void setAuthors(Book book, List<Author> authors) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<Book> update = builder.createCriteriaUpdate(Book.class);
    Root<Book> root = update.from(Book.class);
    update.set(root.get(Book_.authors), authors);
    update.where(builder.equal(root.get(Book_.id), book.getId()));
    entityManager.createQuery(update).executeUpdate();
    entityManager.getTransaction().commit();
  }
}
