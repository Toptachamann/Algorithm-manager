package app.dao.interf;

import app.model.Author;
import app.model.Book;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface BookDao {
  Book createBook(
      String title,
      @Nullable Integer volume,
      Integer edition,
      List<Author> authors)
      throws SQLException;

  List<Book> getBooks() throws Exception;

  List<Book> searchBooks(String title, Integer volume, Integer edition, List<Author> authors)
      throws Exception;

  void setTitle(Book book, String title) throws Exception;

  void setVolume(Book book, Integer volume) throws Exception;

  void setEdition(Book book, int edition) throws Exception;

  void deleteBook(Book book) throws Exception;

  void addAuthors(Book book, List<Author> authors) throws Exception;

  void setAuthors(Book book, List<Author> authors) throws Exception;
}
