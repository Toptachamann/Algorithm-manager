package app.service;

import app.auxiliary.LogicException;
import app.dao.interf.AuthorDao;
import app.dao.interf.BookDao;
import app.dao.interf.ReferenceDao;
import app.model.Algorithm;
import app.model.Author;
import app.model.Book;
import app.model.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
  private static final Logger logger = LogManager.getLogger(BookServiceImpl.class);

  private BookDao bookDao;
  private AuthorDao authorDao;
  private ReferenceDao referenceDao;

  public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, ReferenceDao referenceDao) {
    this.bookDao = bookDao;
    this.authorDao = authorDao;
    this.referenceDao = referenceDao;
  }

  @Override
  public Book createBook(String title, Integer volume, Integer edition, List<Author> authors)
      throws Exception {
    fixIds(authors);
    return bookDao.createBook(title, volume, edition, authors);
  }

  @Override
  public Author createAuthor(String firstName, String lastName) throws Exception {
    return authorDao.createAuthor(firstName, lastName);
  }

  @Override
  public List<Book> getAllBooks() throws Exception {
    return bookDao.getBooks();
  }

  @Override
  public List<Book> searchBooks(String title, Integer edition, Integer volume, List<Author> authors)
      throws Exception {
    return bookDao.searchBooks(title, edition, volume, authors);
  }

  private void fixIds(List<Author> authors) throws Exception {
    for (Author author : authors) {
      Optional<Author> optionalAuthor =
          authorDao.getAuthorByName(author.getFirstName(), author.getLastName());
      if (optionalAuthor.isPresent()) {
        author.setId(optionalAuthor.get().getId());
      } else {
        Author created = authorDao.createAuthor(author.getFirstName(), author.getLastName());
        author.setId(created.getId());
      }
    }
  }

  @Override
  public void updateTitle(Book book, String newValue) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(newValue));
    bookDao.setTitle(book, newValue);
  }

  @Override
  public void updateVolume(Book book, Integer volume) throws Exception {
    if (volume != null) {
      Validate.isTrue(volume > 0);
    }
    bookDao.setVolume(book, volume);
  }

  @Override
  public void updateEdition(Book book, Integer edition) throws Exception {
    Validate.isTrue(edition > 0);
    bookDao.setEdition(book, edition);
  }

  @Override
  public List<Author> setAuthors(Book book, List<Author> newValue) throws Exception {
    fixIds(newValue);
    bookDao.setAuthors(book, newValue);
    return newValue;
  }

  @Override
  public void deleteBook(Book book) throws Exception {
    bookDao.deleteBook(book);
  }

  @Override
  public void createReference(Algorithm algorithm, Book book) throws Exception {
    if (referenceDao.containsReference(algorithm, book)) {
      throw new LogicException("This reference already exists");
    } else {
      referenceDao.createReference(algorithm, book);
    }
  }

  @Override
  public List<Reference> getReferences(Algorithm algorithm) throws Exception {
    return referenceDao.getAlgorithmReferences(algorithm);
  }

  @Override
  public void deleteReference(Algorithm algorithm, Book book) throws Exception {
    if (!referenceDao.containsReference(algorithm, book)) {
      throw new LogicException("This reference doesn't exist");
    } else {
      referenceDao.deleteReference(algorithm, book);
    }
  }
}
