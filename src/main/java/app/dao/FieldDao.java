package app.dao;

import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface FieldDao {
  FieldOfStudy insertFieldOfStudy(String fieldOfStudy) throws SQLException;

  FieldOfStudy insertFieldOfStudy(String fieldOfStudy, String description) throws SQLException;

  List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException;

  Optional<FieldOfStudy> getFieldByName(String name) throws SQLException;

  Optional<FieldOfStudy> getFieldById(int id) throws SQLException;
}
