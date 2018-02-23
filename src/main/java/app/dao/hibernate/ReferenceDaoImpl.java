package app.dao.hibernate;

import app.dao.interf.ReferenceDao;
import app.model.Algorithm;
import app.model.Book;
import app.model.Reference;

import java.sql.SQLException;

public class ReferenceDaoImpl  extends AbstractDao implements ReferenceDao {
  @Override
  public Reference createReference(Algorithm algorithm, Book book) throws SQLException {
    return null;
  }

  @Override
  public boolean containsReference(Algorithm algorithm, Book book) throws SQLException {
    return false;
  }

  @Override
  public void deleteReference(Algorithm algorithm, Book book) throws SQLException {

  }
}
