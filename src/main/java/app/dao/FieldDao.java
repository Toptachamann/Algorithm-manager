package app.dao;

import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;

public interface FieldDao {
  void insertFieldOfStudy(String fieldOfStudy) throws SQLException;

  void insertFieldOfStudy(String fieldOfStudy, String description) throws SQLException;

  List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException;

  FieldOfStudy getFieldByName(String name) throws SQLException;
}
