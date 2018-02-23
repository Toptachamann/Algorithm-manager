package app.dao.hibernate;

import app.dao.interf.BookDao;
import app.model.Author;
import app.model.Book;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public class BookDaoImpl  extends AbstractDao implements BookDao {
  @Override
  public Book createBook(String title, @Nullable Integer volume, Integer edition, List<Author> authors) throws SQLException {
    return null;
  }

  @Override
  public List<Book> getBooks() throws SQLException {
    return null;
  }

  @Override
  public List<Book> searchBook(String title, Integer volume, Integer edition, List<Author> authors) throws SQLException {
    return null;
  }

  @Override
  public <T> void updateBook(String column, T value, int id) throws SQLException {

  }

  @Override
  public void deleteBookById(int id) throws SQLException {

  }

  @Override
  public void addAuthors(int bookId, List<Author> authors) throws SQLException {

  }

  @Override
  public void setAuthors(int bookId, List<Author> authors) throws SQLException {

  }

  @Override
  public List<Book> getBooksByAlgorithm(int algorithmId) throws SQLException {
    return null;
  }
}
