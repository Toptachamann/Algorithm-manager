package app.service;

import app.auxiliary.LogicException;
import app.dao.interf.AuthorDao;
import app.dao.interf.BookDao;
import app.dao.interf.ReferenceDao;
import app.model.Algorithm;
import app.model.Author;
import app.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
    return bookDao.createBook(title, volume, edition, fixIds(authors));
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
    return bookDao.searchBook(title, edition, volume, authors);
  }

  private List<Author> fixIds(List<Author> authors) throws Exception {
    List<Author> authorsList = new ArrayList<>();
    for (Author author : authors) {
      authorsList.add(authorDao.createAuthor(author.getFirstName(), author.getLastName()));
    }
    return authorsList;
  }

  @Override
  public void updateTitle(Book book, String newValue) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(newValue));
    bookDao.updateBook("title", newValue, book.getId());
  }

  @Override
  public void updateVolume(Book book, Integer volume) throws Exception {
    if (volume == null) {
      bookDao.updateBook("volume", null, book.getId());
    } else {
      Validate.isTrue(volume > 0);
      bookDao.updateBook("volume", volume, book.getId());
    }
  }

  @Override
  public void updateEdition(Book book, Integer edition) throws Exception {
    Validate.isTrue(edition > 0);
    bookDao.updateBook("edition", edition, book.getId());
  }

  @Override
  public List<Author> setAuthors(Book book, List<Author> newValue) throws Exception {
    List<Author> authors = fixIds(newValue);
    bookDao.setAuthors(book.getId(), authors);
    return authors;
  }

  @Override
  public void deleteBook(Book book) throws Exception {
    bookDao.deleteBookById(book.getId());
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
  public List<Book> getReferences(Algorithm algorithm) throws Exception {
    return bookDao.getBooksByAlgorithm(algorithm.getId());
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
