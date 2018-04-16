package com.algorithm.manager.dao.jdbc;

import com.algorithm.manager.auxiliary.Util;
import com.algorithm.manager.dao.interf.BookDao;
import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Book;
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
  private static final Logger logger = LogManager.getLogger("BookDaoImpl");

  private PreparedStatement createBook;
  private PreparedStatement allBooks;
  private PreparedStatement bookById;
  private PreparedStatement bookAuthors;
  private PreparedStatement mergeBook;
  private PreparedStatement resetAuthors;
  private PreparedStatement deleteBook;
  private PreparedStatement deleteByTitle;

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
    bookById =
        connection.prepareStatement(
            "SELECT book_id, title, volume, edition FROM book WHERE book_id = ?");
    bookAuthors =
        connection.prepareStatement(
            "SELECT author_id, first_name, last_name FROM author "
                + "inner join textbook on author.author_id = textbook.txtbk_author_id WHERE textbook.txtbk_book_id = ?");
    mergeBook =
        connection.prepareStatement(
            "UPDATE book SET title = ?, volume = ?, edition = ? WHERE author_id = ?");
    createBook =
        connection.prepareStatement("INSERT INTO book (title, volume, edition) VALUE (?, ?, ?)");
    deleteBook = connection.prepareStatement("DELETE FROM book WHERE book_id = ?");
    resetAuthors =
        connection.prepareStatement(
            "DELETE FROM algorithms.textbook WHERE algorithms.textbook.txtbk_book_id = ?");
    deleteByTitle =
        connection.prepareStatement("DELETE FROM algorithms.book WHERE algorithms.book.title = ?");
  }

  @Override
  public void persist(Book book) throws SQLException {
    try {
      createBook.setString(1, book.getTitle());
      if (book.getVolume() == null) {
        createBook.setNull(2, Types.INTEGER);
      } else {
        createBook.setInt(2, book.getVolume());
      }
      createBook.setInt(3, book.getEdition());
      createBook.executeUpdate();
      book.setId(getLastId(connection));
      addAuthorsToBook(book, book.getAuthors());
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persistAlgorithm a book {}", book);
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
    logger.debug(() -> Util.format(bookById));
    ResultSet set = bookById.executeQuery();
    if (set.next()) {
      Integer volume = set.getInt(3);
      if (set.wasNull()) {
        volume = null;
      }
      Book book = new Book(set.getInt(1), set.getString(2), volume, set.getInt(4));
      book.setAuthors(getBookAuthors(book));
      return Optional.of(book);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void merge(Book book) throws SQLException {
    try {
      mergeBook.setString(1, book.getTitle());
      if (book.getVolume() == null) {
        mergeBook.setNull(2, Types.INTEGER);
      } else {
        mergeBook.setInt(2, book.getVolume());
      }
      mergeBook.setInt(1, book.getEdition());
      mergeBook.setInt(4, book.getId());
      mergeBook.executeUpdate();
      resetAuthors(book);
      addAuthorsToBook(book, book.getAuthors());
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to merge book {}", book);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void delete(Book book) throws SQLException {
    try {
      deleteBook.setInt(1, book.getId());
      logger.debug(() -> Util.format(deleteBook));
      deleteBook.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to deleteById book ", book);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void deleteByTitle(Book book) throws Exception {
    try {
      deleteByTitle.setString(1, book.getTitle());
      logger.debug(() -> Util.format(deleteByTitle));
      deleteByTitle.executeUpdate();
      connection.commit();
    } catch (Exception e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete book {} by title", book);
      rollBack(connection);
      throw e;
    }
  }

  private void addAuthorsToBook(Book book, List<Author> authors) throws SQLException {
    if (authors.size() > 0) {
      StringBuilder builder =
          new StringBuilder("INSERT INTO textbook (txtbk_book_id, txtbk_author_id) VALUES ");
      for (Author author : authors) {
        builder
            .append("(")
            .append(book.getId())
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

  private List<Author> getBookAuthors(Book book) throws SQLException {
    bookAuthors.setInt(1, book.getId());
    logger.debug(() -> Util.format(bookAuthors));
    ResultSet set = bookAuthors.executeQuery();
    List<Author> result = new ArrayList<>();
    while (set.next()) {
      result.add(new Author(set.getInt(1), set.getString(2), set.getString(3)));
    }
    return result;
  }

  private void resetAuthors(Book book) throws SQLException {
    try {
      resetAuthors.setInt(1, book.getId());
      logger.debug(() -> Util.format(resetAuthors));
      resetAuthors.executeUpdate();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to reset authors of the book", book);
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
