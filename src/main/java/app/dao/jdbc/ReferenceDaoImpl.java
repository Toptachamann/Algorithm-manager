package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.ReferenceDao;
import app.model.Algorithm;
import app.model.Book;
import app.model.Reference;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReferenceDaoImpl extends AbstractDao implements ReferenceDao {
  private static final Logger logger = LogManager.getLogger(ReferenceDaoImpl.class);

  private PreparedStatement countReferences;
  private PreparedStatement createReference;
  private PreparedStatement deleteReference;

  public ReferenceDaoImpl() throws SQLException {
    countReferences =
        connection.prepareStatement(
            "SELECT COUNT(*) FROM algorithm_reference WHERE ref_algorithm_id = ? AND ref_book_id = ?");
    createReference =
        connection.prepareStatement(
            "INSERT INTO algorithm_reference (ref_algorithm_id, ref_book_id) VALUE (?, ?)");
    deleteReference =
        connection.prepareStatement(
            "DELETE FROM algorithm_reference WHERE algorithms.algorithm_reference.ref_algorithm_id = ? AND ref_book_id = ?");
  }

  @Override
  public Reference createReference(Algorithm algorithm, Book book) throws SQLException {
    try {
      createReference.setInt(1, algorithm.getId());
      createReference.setInt(2, book.getId());
      logger.debug(() -> Util.format(createReference));
      createReference.executeUpdate();
      int id = getLastId(connection);
      connection.commit();
      return new Reference(id, algorithm, book);
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to create reference with algorithm_id = {} and book_id = {}", algorithm, book);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public boolean containsReference(Algorithm algorithm, Book book) throws SQLException {
    countReferences.setInt(1, algorithm.getId());
    countReferences.setInt(2, book.getId());
    logger.debug(() -> Util.format(countReferences));
    ResultSet set = countReferences.executeQuery();
    set.next();
    return set.getInt(1) == 1;
  }

  @Override
  public void deleteReference(Algorithm algorithm, Book book) throws SQLException {
    try {
      deleteReference.setInt(1, algorithm.getId());
      deleteReference.setInt(2, book.getId());
      logger.debug(() -> Util.format(deleteReference));
      deleteReference.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to delete reference with algorithm_id = {} and book_id = {}", algorithm, book);
      rollBack(connection);
      throw e;
    }
  }
}
