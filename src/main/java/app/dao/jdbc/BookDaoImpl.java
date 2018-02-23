package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.BookDao;
import app.model.Author;
import app.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl extends AbstractDao implements BookDao {
  private static final Logger logger = LogManager.getLogger(BookDaoImpl.class);

  private PreparedStatement allBooks;
  private PreparedStatement createBook;
  private PreparedStatement connectAlgorithm;
  private PreparedStatement getBooksByAlgorithm;
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
    createBook =
        connection.prepareStatement("INSERT INTO book (title, volume, edition) VALUE (?, ?, ?)");
    connectAlgorithm =
        connection.prepareStatement(
            "INSERT INTO algorithm_reference (ref_textbook_id, ref_algorithm_id) "
                + "VALUE (?, ?)");
    getBooksByAlgorithm =
        connection.prepareStatement(
            "SELECT book_id, title, volume, edition, author_id, first_name, last_name\n"
                + "FROM (SELECT ref_book_id FROM algorithm_reference WHERE ref_algorithm_id = ?) AS algos\n"
                + "INNER JOIN book ON algos.ref_book_id = book_id INNER JOIN textbook ON book_id = txtbk_book_id \n"
                + "INNER JOIN author ON txtbk_author_id = author_id\n"
                + "ORDER BY book_id, author_id");
    deleteBook = connection.prepareStatement("DELETE FROM book WHERE book_id = ?");
    resetAuthors =
        connection.prepareStatement(
            "DELETE FROM algorithms.textbook WHERE algorithms.textbook.txtbk_book_id = ?");
  }

  @Override
  public Book createBook(
      String title, @Nullable Integer volume, Integer edition, List<Author> authors)
      throws SQLException {
    try {
      createBook.setString(1, title.trim());
      if (volume == null) {
        createBook.setNull(2, Types.INTEGER);
      } else {
        createBook.setInt(2, volume);
      }
      createBook.setInt(3, edition);
      createBook.executeUpdate();
      int bookId = getLastId(connection);
      addAuthorsToBook(bookId, authors);
      connection.commit();
      return new Book(bookId, title, volume, edition, authors);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to create a book: title = {}, volume = {}, edition = {}, authors = {}",
          title,
          String.valueOf(volume),
          edition,
          authors.toString());
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
  public List<Book> searchBook(String title, Integer volume, Integer edition, List<Author> authors)
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
      return booksFromSet(statement.executeQuery(query));
    }
  }

  @Override
  public <T> void updateBook(String column, T newValue, int id) throws SQLException {
    try {
      String query = "UPDATE book SET " + column + " = ";
      if (newValue instanceof Integer) {
        query += newValue;
      } else {
        query += Util.fixForString(newValue.toString());
      }
      query += " WHERE book_id = " + id;
      String query1 = query;
      logger.debug(() -> Util.format(query1));
      connection.createStatement().executeUpdate(query);
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to set value {} in column {} in table book where id = {}", newValue, column, id);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void deleteBookById(int bookId) throws SQLException {
    try {
      deleteBook.setInt(1, bookId);
      logger.debug(() -> Util.format(deleteBook));
      deleteBook.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete book with id = {}", bookId);
      rollBack(connection);
      throw e;
    }
  }

  private void resetAuthors(int bookId) throws SQLException {
    try {
      resetAuthors.setInt(1, bookId);
      logger.debug(() -> Util.format(resetAuthors));
      resetAuthors.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to reset book's authors:  book_id = {}", bookId);
      rollBack(connection);
      throw e;
    }
  }

  private void addAuthorsToBook(int bookId, List<Author> authors) throws SQLException {
    if (authors.size() > 0) {
      StringBuilder builder =
          new StringBuilder("INSERT INTO book (txtbk_book_id, txtbk_author_id) VALUES ");
      for (Author author : authors) {
        builder.append("(").append(bookId).append(", ").append(author.getId()).append(")");
      }
      String query = builder.toString();
      logger.debug(() -> Util.format(query));
      connection.createStatement().executeUpdate(query);
    }
  }

  @Override
  public void addAuthors(int bookId, List<Author> authors) throws SQLException {
    try {
      addAuthorsToBook(bookId, authors);
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to add authors {} to book with id {}", authors.toString(), bookId);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void setAuthors(int bookId, List<Author> authors) throws SQLException {
    resetAuthors(bookId);
    addAuthors(bookId, authors);
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

  @Override
  public List<Book> getBooksByAlgorithm(int algorithmId) throws SQLException {
    getBooksByAlgorithm.setInt(1, algorithmId);
    logger.debug(() -> Util.format(getBooksByAlgorithm));
    return booksFromSet(getBooksByAlgorithm.executeQuery());
  }
}
