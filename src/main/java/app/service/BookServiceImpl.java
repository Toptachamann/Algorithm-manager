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
    Book book = new Book(title, volume, edition, authors);
    bookDao.persist(book);
    return book;
  }

  @Override
  public Author createAuthor(String firstName, String lastName) throws Exception {
    Author author = new Author(firstName, lastName);
    authorDao.persist(author);
    return author;
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
          authorDao.getAuthor(author.getFirstName(), author.getLastName());
      if (optionalAuthor.isPresent()) {
        author.setId(optionalAuthor.get().getId());
      } else {
        authorDao.persist(author);
      }
    }
  }

  @Override
  public void updateTitle(Book book, String newValue) throws Exception {
    Validate.isTrue(!StringUtils.isBlank(newValue));
    book.setTitle(newValue);
    bookDao.merge(book);
  }

  @Override
  public void updateVolume(Book book, Integer volume) throws Exception {
    if (volume != null) {
      Validate.isTrue(volume > 0);
    }
    book.setVolume(volume);
    bookDao.merge(book);
  }

  @Override
  public void updateEdition(Book book, Integer edition) throws Exception {
    Validate.isTrue(edition > 0);
    book.setEdition(edition);
    bookDao.merge(book);
  }

  @Override
  public List<Author> setAuthors(Book book, List<Author> newValue) throws Exception {
    fixIds(newValue);
    book.setAuthors(newValue);
    bookDao.merge(book);
    return newValue;
  }

  @Override
  public void deleteBook(Book book) throws Exception {
    bookDao.delete(book);
  }

  @Override
  public Reference createReference(Algorithm algorithm, Book book) throws Exception {
    if (referenceDao.containsReference(algorithm, book)) {
      throw new LogicException("This reference already exists");
    } else {
      Reference reference = new Reference(algorithm, book);
      referenceDao.persist(reference);
      return reference;
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
      referenceDao.delete(new Reference(algorithm, book));
    }
  }
}
