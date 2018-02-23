package app.dao.hibernate;

import app.dao.interf.FieldDao;
import app.model.FieldOfStudy;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FieldDaoImpl  extends AbstractDao implements FieldDao {
  @Override
  public FieldOfStudy createFieldOfStudy(String fieldOfStudy) throws SQLException {
    return null;
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String fieldOfStudy, String description) throws SQLException {
    return null;
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException {
    return null;
  }

  @Override
  public Optional<FieldOfStudy> getFieldByName(String name) throws SQLException {
    return Optional.empty();
  }

  @Override
  public Optional<FieldOfStudy> getFieldById(int id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public void updateFieldOfStudy(String newName, int id) throws SQLException {

  }
}
