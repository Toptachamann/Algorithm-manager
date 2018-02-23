package app.service;

import app.auxiliary.LogicException;
import app.dao.interf.BookDao;
import app.dao.interf.ReferenceDao;
import app.dao.interf.AuthorDao;
import app.model.Algorithm;
import app.model.Author;
import app.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements TextbookService {
  private static final Logger logger = LogManager.getLogger(BookServiceImpl.class);

  private BookDao bookDao;
  private AuthorDao authorDao;
  private ReferenceDao referenceDao;

  public BookServiceImpl(
      BookDao bookDao, AuthorDao authorDao, ReferenceDao referenceDao) {
    this.bookDao = bookDao;
    this.authorDao = authorDao;
    this.referenceDao = referenceDao;
  }

  @Override
  public Book createBook(String title, Integer volume, Integer edition, List<Author> authors)
      throws SQLException {
    return bookDao.createBook(title, volume, edition, fixIds(authors));
  }

  @Override
  public Author createAuthor(String firstName, String lastName) throws SQLException {
    return authorDao.createAuthor(firstName, lastName);
  }

  @Override
  public List<Book> getAllBooks() throws SQLException {
    return bookDao.getBooks();
  }

  @Override
  public List<Book> searchBooks(
      String title, Integer edition, Integer volume, List<Author> authors) throws SQLException {
    return bookDao.searchBook(title, edition, volume, authors);
  }

  private List<Author> fixIds(List<Author> authors) throws SQLException {
    List<Author> authorsList = new ArrayList<>();
    for (Author author : authors) {
      authorsList.add(authorDao.createAuthor(author.getFirstName(), author.getLastName()));
    }
    return authorsList;
  }

  @Override
  public void updateTitle(Book book, String newValue) throws SQLException {
    Validate.isTrue(!StringUtils.isBlank(newValue));
    bookDao.updateBook("title", newValue, book.getId());
  }

  @Override
  public void updateVolume(Book book, Integer volume) throws SQLException {
    if (volume == null) {
      bookDao.updateBook("volume", null, book.getId());
    } else {
      Validate.isTrue(volume > 0);
      bookDao.updateBook("volume", volume, book.getId());
    }
  }

  @Override
  public void updateEdition(Book book, Integer edition) throws SQLException {
    Validate.isTrue(edition > 0);
    bookDao.updateBook("edition", edition, book.getId());
  }

  @Override
  public List<Author> setAuthors(Book book, List<Author> newValue) throws SQLException {
    List<Author> authors = fixIds(newValue);
    bookDao.setAuthors(book.getId(), authors);
    return authors;
  }

  @Override
  public void deleteBook(Book book) throws SQLException {
    bookDao.deleteBookById(book.getId());
  }

  @Override
  public void createReference(Algorithm algorithm, Book book) throws SQLException, LogicException {
    if (referenceDao.containsReference(algorithm, book)) {
      throw new LogicException("This reference already exists");
    } else {
      referenceDao.createReference(algorithm, book);
    }
  }

  @Override
  public List<Book> getReferences(Algorithm algorithm) throws SQLException {
    return bookDao.getBooksByAlgorithm(algorithm.getId());
  }

  @Override
  public void deleteReference(Algorithm algorithm, Book book) throws SQLException, LogicException {
    if (!referenceDao.containsReference(algorithm, book)) {
      throw new LogicException("This reference doesn't exist");
    } else {
      referenceDao.deleteReference(algorithm, book);
    }
  }
}
