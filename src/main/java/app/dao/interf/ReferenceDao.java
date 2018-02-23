package app.dao.interf;

import app.model.Algorithm;
import app.model.Reference;
import app.model.Book;

import java.sql.SQLException;

public interface ReferenceDao {

  Reference createReference(Algorithm algorithm, Book book) throws Exception;


  boolean containsReference(Algorithm algorithm, Book book) throws Exception;

  void deleteReference(Algorithm algorithm, Book book) throws Exception;
}
