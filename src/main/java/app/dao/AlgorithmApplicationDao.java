package app.dao;

import java.sql.SQLException;

public interface AlgorithmApplicationDao {

  void createApplication(int algorithmId, int areaId) throws SQLException;

  boolean containsApplication(int algorithmId, int areaId) throws SQLException;

  void deleteApplication(int algorithmId, int areaId) throws SQLException;
}
