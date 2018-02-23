package app.service;

import app.auxiliary.LogicException;
import app.dao.AlgorithmReferenceDao;
import app.dao.AuthorDao;
import app.dao.TextbookDao;
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

public class TextbookServiceImpl implements TextbookService {
  private static final Logger logger = LogManager.getLogger(TextbookServiceImpl.class);

  private TextbookDao textbookDao;
  private AuthorDao authorDao;
  private AlgorithmReferenceDao referenceDao;

  public TextbookServiceImpl(
      TextbookDao textbookDao, AuthorDao authorDao, AlgorithmReferenceDao referenceDao) {
    this.textbookDao = textbookDao;
    this.authorDao = authorDao;
    this.referenceDao = referenceDao;
  }

  @Override
  public Book createTextbook(String title, Integer volume, Integer edition, List<Author> authors)
      throws SQLException {
    return textbookDao.insertTextbook(title, volume, edition, fixIds(authors));
  }

  @Override
  public Author createAuthor(String firstName, String lastName) throws SQLException {
    return authorDao.createAuthor(firstName, lastName);
  }

  @Override
  public List<Book> getAllTextbooks() throws SQLException {
    return textbookDao.getTextbooks();
  }

  @Override
  public List<Book> searchTextbooks(
      String title, Integer edition, Integer volume, List<Author> authors) throws SQLException {
    return textbookDao.searchTextbook(title, edition, volume, authors);
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
    textbookDao.updateBook("title", newValue, book.getId());
  }

  @Override
  public void updateVolume(Book book, Integer volume) throws SQLException {
    if (volume == null) {
      textbookDao.updateBook("volume", null, book.getId());
    } else {
      Validate.isTrue(volume > 0);
      textbookDao.updateBook("volume", volume, book.getId());
    }
  }

  @Override
  public void updateEdition(Book book, Integer edition) throws SQLException {
    Validate.isTrue(edition > 0);
    textbookDao.updateBook("edition", edition, book.getId());
  }

  @Override
  public List<Author> setAuthors(Book book, List<Author> newValue) throws SQLException {
    List<Author> authors = fixIds(newValue);
    textbookDao.setAuthors(book.getId(), authors);
    return authors;
  }

  @Override
  public void deleteTextbook(Book book) throws SQLException {
    textbookDao.deleteTextbookById(book.getId());
  }

  @Override
  public void createReference(Algorithm algorithm, Book book) throws SQLException, LogicException {
    if (referenceDao.containsReference(algorithm.getId(), book.getId())) {
      throw new LogicException("This reference already exists");
    } else {
      referenceDao.createReference(algorithm.getId(), book.getId());
    }
  }

  @Override
  public List<Book> getReferences(Algorithm algorithm) throws SQLException {
    return textbookDao.getTextbooksByAlgorithm(algorithm.getId());
  }

  @Override
  public void deleteReference(Algorithm algorithm, Book book) throws SQLException, LogicException {
    if (!referenceDao.containsReference(algorithm.getId(), book.getId())) {
      throw new LogicException("This reference doesn't exist");
    } else {
      referenceDao.deleteReference(algorithm.getId(), book.getId());
    }
  }
}
