package app.dao.interf;

import app.model.Algorithm;
import app.model.Reference;
import app.model.Book;

import java.sql.SQLException;

public interface ReferenceDao {

  Reference createReference(Algorithm algorithm, Book book) throws SQLException;


  boolean containsReference(Algorithm algorithm, Book book) throws SQLException;

  void deleteReference(Algorithm algorithm, Book book) throws SQLException;
}
