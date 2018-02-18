package app.service;

import app.auxiliary.LogicException;
import app.dao.AuthorDao;
import app.dao.TextbookDao;
import app.model.Algorithm;
import app.model.Author;
import app.model.Textbook;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TextbookServiceImpl implements TextbookService {
  private static final Logger logger = LogManager.getLogger(TextbookServiceImpl.class);

  private final TextbookDao textbookDao;
  private final AuthorDao authorDao;

  public TextbookServiceImpl(TextbookDao textbookDao, AuthorDao authorDao) {
    this.textbookDao = textbookDao;
    this.authorDao = authorDao;
  }

  @Override
  public Textbook createTextbook(
      String title, Integer volume, Integer edition, List<Author> authors) throws SQLException {
    return textbookDao.insertTextbook(title, volume, edition, fixIds(authors));
  }

  @Override
  public Author createAuthor(String firstName, String lastName) throws SQLException {
    return authorDao.createAuthor(firstName, lastName);
  }

  @Override
  public List<Textbook> getAllTextbooks() throws SQLException {
    return textbookDao.getTextbooks();
  }

  @Override
  public List<Textbook> searchTextbooks(
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
  public void updateTitle(Textbook textbook, String newValue) throws SQLException {
    Validate.isTrue(!StringUtils.isBlank(newValue));
    textbookDao.updateBook("title", newValue, textbook.getId());
  }

  @Override
  public void updateVolume(Textbook textbook, Integer volume) throws SQLException {
    if (volume == null) {
      textbookDao.updateBook("volume", null, textbook.getId());
    } else {
      Validate.isTrue(volume > 0);
      textbookDao.updateBook("volume", volume, textbook.getId());
    }
  }

  @Override
  public void updateEdition(Textbook textbook, Integer edition) throws SQLException {
    Validate.isTrue(edition > 0);
    textbookDao.updateBook("edition", edition, textbook.getId());
  }

  @Override
  public List<Author> setAuthors(Textbook textbook, List<Author> newValue) throws SQLException {
    List<Author> authors = fixIds(newValue);
    textbookDao.setAuthors(textbook.getId(), authors);
    return authors;
  }

  @Override
  public void deleteTextbook(Textbook textbook) throws SQLException {
    textbookDao.deleteTextbookById(textbook.getId());
  }

  @Override
  public void createReference(Algorithm algorithm, Textbook textbook)
      throws SQLException, LogicException {
    if (textbookDao.containsReference(algorithm.getId(), textbook.getId())) {
      throw new LogicException("This reference already exists");
    } else {
      textbookDao.createReference(algorithm.getId(), textbook.getId());
    }
  }

  @Override
  public List<Textbook> getReferences(Algorithm algorithm) throws SQLException {
    return textbookDao.getTextbooksByAlgorithm(algorithm.getId());
  }

  @Override
  public void deleteReference(Algorithm algorithm, Textbook textbook)
      throws SQLException, LogicException {
    if (!textbookDao.containsReference(algorithm.getId(), textbook.getId())) {
      throw new LogicException("This reference doesn't exist");
    } else {
      textbookDao.deleteReference(algorithm.getId(), textbook.getId());
    }
  }
}
