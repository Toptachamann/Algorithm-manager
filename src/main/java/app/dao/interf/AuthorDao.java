package app.dao.interf;

import app.model.Author;

import java.sql.SQLException;
import java.util.List;

public interface AuthorDao {
  List<Author> getAuthorByName(String firstName, String lastName) throws Exception;

  int getAuthorId(String firstName, String lastName) throws Exception;

  Author createAuthor(String firstName, String lastName) throws Exception;
}
