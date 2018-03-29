package app.dao.interf;

import app.model.FieldOfStudy;

import java.util.List;
import java.util.Optional;

public interface FieldDao {
  FieldOfStudy createFieldOfStudy(String fieldOfStudy) throws Exception;

  FieldOfStudy createFieldOfStudy(String fieldOfStudy, String description) throws Exception;

  List<FieldOfStudy> getAllFieldsOfStudy() throws Exception;

  Optional<FieldOfStudy> getFieldByName(String name) throws Exception;

  Optional<FieldOfStudy> getFieldById(int id) throws Exception;

  default boolean containsFieldOfStudy(int id) throws Exception {
    return getFieldById(id).isPresent();
  }

  default boolean containsFieldOfStudy(String name) throws Exception {
    return getFieldByName(name).isPresent();
  }

  void updateFieldOfStudy(String newName, int id) throws Exception;

  void deleteFieldOfStudy(int id) throws Exception;
}
