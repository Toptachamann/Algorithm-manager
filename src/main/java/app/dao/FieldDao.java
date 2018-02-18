package app.dao;

import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface FieldDao {
  FieldOfStudy createFieldOfStudy(String fieldOfStudy) throws SQLException;

  FieldOfStudy createFieldOfStudy(String fieldOfStudy, String description) throws SQLException;

  List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException;

  Optional<FieldOfStudy> getFieldByName(String name) throws SQLException;

  Optional<FieldOfStudy> getFieldById(int id) throws SQLException;

  void updateFieldOfStudy(String newName, int id) throws SQLException;
}
