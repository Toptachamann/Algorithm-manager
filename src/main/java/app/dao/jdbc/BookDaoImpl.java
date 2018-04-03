package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.BookDao;
import app.model.Author;
import app.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl extends AbstractDao implements BookDao {
  private static final Logger logger = LogManager.getLogger("app.dao.jdbc.BookDaoImpl");

  private PreparedStatement allBooks;
  private PreparedStatement bookById;
  private PreparedStatement createBook;
  private PreparedStatement setTitle;
  private PreparedStatement setVolume;
  private PreparedStatement setEdition;
  private PreparedStatement deleteBook;
  private PreparedStatement resetAuthors;

  public BookDaoImpl() throws SQLException {
    allBooks =
        connection.prepareStatement(
            "SELECT \n"
                + "    book_id, title, volume, edition, author_id, first_name, last_name\n"
                + "FROM\n"
                + "    ((book\n"
                + "    INNER JOIN textbook ON book_id = txtbk_book_id)\n"
                + "    INNER JOIN author ON txtbk_author_id = author_id)\n"
                + "ORDER BY book_id, author_id");
    bookById = connection.prepareStatement("SELECT \n"
        + "    book_id, title, volume, edition, author_id, first_name, last_name\n"
        + "FROM\n"
        + "    ((book\n"
        + "    INNER JOIN textbook ON book_id = txtbk_book_id)\n"
        + "    INNER JOIN author ON txtbk_author_id = author_id)\n"
        + "WHERE book_id = ?"
        + "ORDER BY book_id, author_id");
    createBook =
        connection.prepareStatement("INSERT INTO book (title, volume, edition) VALUE (?, ?, ?)");
    deleteBook = connection.prepareStatement("DELETE FROM book WHERE book_id = ?");
    setTitle = connection.prepareStatement("UPDATE book SET title = ? WHERE book_id = ?");
    setVolume = connection.prepareStatement("UPDATE book SET volume = ? WHERE book_id = ?");
    setEdition = connection.prepareStatement("UPDATE book SET edition = ? WHERE book_id = ?");
    resetAuthors =
        connection.prepareStatement(
            "DELETE FROM algorithms.textbook WHERE algorithms.textbook.txtbk_book_id = ?");
  }

  @Override
  public void persist(Book book) throws SQLException {
    try {
      createBook.setString(1,book.getTitle());
      if (book.getVolume() == null) {
        createBook.setNull(2, Types.INTEGER);
      } else {
        createBook.setInt(2, book.getVolume());
      }
      createBook.setInt(3, book.getEdition());
      createBook.executeUpdate();
      book.setId(getLastId(connection));
      addAuthorsToBook(book.getId(), book.getAuthors());
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to persist a book {}", book);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public List<Book> getBooks() throws SQLException {
    logger.debug(() -> Util.format(allBooks));
    return booksFromSet(allBooks.executeQuery());
  }

  @Override
  public List<Book> searchBooks(String title, Integer volume, Integer edition, List<Author> authors)
      throws SQLException {
    StringBuilder builder = new StringBuilder();
    if (!StringUtils.isBlank(title)) {
      builder.append(" AND b1.title LIKE ").append(Util.fixForLike(title.trim()));
    }
    if (volume != null && volume > 0) {
      builder.append(" AND b1.volume = ").append(volume);
    }
    if (edition != null && edition > 0) {
      builder.append(" AND b1.edition = ").append(edition);
    }
    if (authors.size() > 0) {
      builder.append(" AND ( 1 != 1");
      for (Author author : authors) {
        builder
            .append(" OR CONCAT(a1.first_name, a1.last_name) LIKE ")
            .append(Util.fixForLike(author.getFirstName() + " " + author.getLastName()));
      }
      builder.append(")");
    }
    builder.append(")");
    if (builder.length() == 0) {
      return getBooks();
    } else {
      builder.insert(
          0,
          "SELECT \n"
              + "    b2.book_id,\n"
              + "    b2.title,\n"
              + "    b2.volume,\n"
              + "    b2.edition,\n"
              + "    a2.author_id,\n"
              + "    a2.first_name,\n"
              + "    a2.last_name\n"
              + "FROM\n"
              + "    ((SELECT \n"
              + "        b1.book_id, b1.title, b1.edition, b1.volume\n"
              + "    FROM\n"
              + "        ((author AS a1\n"
              + "    INNER JOIN textbook AS t1 ON a1.author_id = t1.txtbk_author_id)\n"
              + "    INNER JOIN book AS b1 ON t1.txtbk_book_id = b1.book_id)"
              + " WHERE 1 = 1 ");
      builder
          .append(" AS b2\n")
          .append("    INNER JOIN textbook AS t2 ON b2.book_id = t2.txtbk_book_id)\n")
          .append("    INNER JOIN author AS a2 ON a2.author_id = t2.txtbk_author_id\n")
          .append("    order by b2.book_id, a2.author_id");
      Statement statement = connection.createStatement();
      String query = builder.toString();
      logger.debug(() -> Util.format(query));
      return logger.traceExit(booksFromSet(statement.executeQuery(query)));
    }
  }

  @Override
  public Optional<Book> getBookById(int id) throws SQLException {
    bookById.setInt(1, id);
    logger.debug(()->Util.format(bookById));
    ResultSet set = bookById.executeQuery();
    if(set.next()){
      return Optional.of(booksFromSet(set).get(0));
    }else{
      return Optional.empty();
    }
  }

  @Override
  public void setTitle(Book book, String title) throws SQLException {
    try {
      setTitle.setString(1, title);
      setTitle.setInt(2, book.getId());
      logger.debug(() -> Util.format(setTitle));
      setTitle.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to set title {} to book {}", title, book);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void setVolume(Book book, Integer volume) throws SQLException {
    try {
      if (volume == null) {
        setVolume.setNull(1, Types.INTEGER);
      } else {
        setVolume.setInt(1, volume);
      }
      setVolume.setInt(2, book.getId());
      logger.debug(() -> Util.format(setVolume));
      setVolume.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to set volume {} to book {}", volume, book);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void setEdition(Book book, int edition) throws SQLException {
    try {
      setEdition.setInt(1, edition);
      setEdition.setInt(2, book.getId());
      logger.debug(() -> Util.format(setEdition));
      setEdition.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to set edition {} to book {}", edition, book);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void deleteBook(Book book) throws SQLException {
    try {
      deleteBook.setInt(1, book.getId());
      logger.debug(() -> Util.format(deleteBook));
      deleteBook.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete book ", book);
      rollBack(connection);
      throw e;
    }
  }

  private void addAuthorsToBook(int bookId, List<Author> authors) throws SQLException {
    if (authors.size() > 0) {
      StringBuilder builder =
          new StringBuilder("INSERT INTO textbook (txtbk_book_id, txtbk_author_id) VALUES ");
      for (Author author : authors) {
        builder
            .append("(")
            .append(bookId)
            .append(", ")
            .append(author.getId())
            .append(")")
            .append(",");
      }
      builder.deleteCharAt(builder.length() - 1);
      String query = builder.toString();
      logger.debug(() -> Util.format(query));
      connection.createStatement().executeUpdate(query);
    }
  }

  @Override
  public void addAuthors(Book book, List<Author> authors) throws SQLException {
    try {
      addAuthorsToBook(book.getId(), authors);
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to add authors {} to book {}", authors.toString(), book);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void setAuthors(Book book, List<Author> authors) throws SQLException {
    try {
      resetAuthors.setInt(1, book.getId());
      logger.debug(() -> Util.format(resetAuthors));
      resetAuthors.executeUpdate();
      addAuthors(book, authors);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to set authors {} to book {}", authors, book);
      rollBack(connection);
      throw e;
    }
  }

  private List<Book> booksFromSet(ResultSet set) throws SQLException {
    List<Book> books = new ArrayList<>();
    if (set.next()) {
      for (; !set.isAfterLast(); ) {
        int volume = set.getInt(3);
        Book book;
        if (set.wasNull()) {
          book = new Book(set.getInt(1), set.getString(2), null, set.getInt(4));
        } else {
          book = new Book(set.getInt(1), set.getString(2), volume, set.getInt(4));
        }
        int id = set.getInt(1);
        do {
          book.addAuthor(new Author(set.getInt(5), set.getString(6), set.getString(7)));
        } while (set.next() && set.getInt(1) == id);
        books.add(book);
      }
    }
    return books;
  }
}
