package app.dao;

import java.sql.SQLException;

public interface AlgorithmReferenceDao {
  void createReference(int algorithmId, int bookId) throws SQLException;

  boolean containsReference(int algorithmId, int bookId) throws SQLException;

  void deleteReference(int algorithmId, int bookId) throws SQLException;
}
