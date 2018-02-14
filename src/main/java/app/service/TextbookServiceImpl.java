package app.service;

import app.dao.AuthorDao;
import app.dao.TextbookDao;
import app.model.Author;
import app.model.Textbook;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TextbookServiceImpl implements TextbookService {
  private static final Logger logger = LogManager.getLogger(TextbookServiceImpl.class);


  private final TextbookDao textbookDao;
  private final AuthorDao authorDao;

  private final String numberPattern = "^\\s*[0-9]+\\s*$";
  private final String authorsPattern = "^(\\s*[a-zA-Z]+\\s+[a-zA-Z]+(,\\s+(?!$)|\\s*$))+";

  public TextbookServiceImpl(TextbookDao textbookDao, AuthorDao authorDao) {
    this.textbookDao = textbookDao;
    this.authorDao = authorDao;
  }

  @Override
  public List<Textbook> searchTextbooks(
      String title, String editionStr, String volumeStr, String authors) throws SQLException {
    Integer edition = null;
    Integer volume = null;

    if (editionStr != null) {
      if (editionStr.matches(numberPattern)) {
        edition = Integer.valueOf(editionStr);
      } else {
        // TODO: alert
      }
    }
    if (volumeStr != null) {
      if (volumeStr.matches(numberPattern)) {
        volume = Integer.valueOf(volumeStr);
      } else {
        // TODO alert
      }
    }
    if (!StringUtils.isBlank(authors) && !authors.matches(authorsPattern)) {
      // TODO alert
    }
    return textbookDao.searchTextbook(title, edition, volume, authors);
  }

  @Override
  public List<Textbook> getAllTextbooks() throws SQLException {
    return textbookDao.getTextbooks();
  }

  @Override
  public void deleteTextbook(Textbook textbook) throws SQLException {
    textbookDao.deleteTextbookById(textbook.getId());
  }

  @Override
  public Textbook createTextbook(String title, String volumeStr, String editionStr, String authors)
      throws SQLException {
    if (StringUtils.isBlank(title)) {
      // TODO alert
    }
    if (!StringUtils.isBlank(volumeStr) && !volumeStr.matches(numberPattern)) {
      // TODO alert
    }
    if (StringUtils.isBlank(editionStr) || editionStr.matches(numberPattern)) {
      // TODO alert
    }
    if (StringUtils.isBlank(authors) || !authors.matches(authorsPattern)) {
      // TODO alert
    }
    Integer volume = Integer.valueOf(volumeStr);
    Integer edition = StringUtils.isBlank(volumeStr) ? null : Integer.valueOf(editionStr);

    String[] authorsArr = authors.split(",\\s*");
    List<Author> authorsList = new ArrayList<>();
    for (String authorStr : authorsArr) {
      String[] authorName = authorStr.split("\\s+");
      authorsList.add(authorDao.insertAuthor(authorName[0], authorName[1]));
    }
    return textbookDao.insertTextbook(title, volume, edition, authorsList);
  }
}
