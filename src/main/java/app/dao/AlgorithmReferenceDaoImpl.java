package app.dao;

import app.auxiliary.Connector;
import app.auxiliary.Util;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlgorithmReferenceDaoImpl extends AbstractDao implements AlgorithmReferenceDao {
  private static final Logger logger = LogManager.getLogger(AlgorithmReferenceDaoImpl.class);

  private Connection connection;
  private PreparedStatement countReferences;
  private PreparedStatement createReference;
  private PreparedStatement deleteReference;

  public AlgorithmReferenceDaoImpl() throws SQLException {
    connection = Connector.getConnection();
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
  public boolean containsReference(int algorithmId, int bookId) throws SQLException {
    countReferences.setInt(1, algorithmId);
    countReferences.setInt(2, bookId);
    logger.debug(() -> Util.format(countReferences));
    ResultSet set = countReferences.executeQuery();
    set.next();
    return set.getInt(1) == 1;
  }

  @Override
  public void createReference(int algorithmId, int bookId) throws SQLException {
    try {
      createReference.setInt(1, algorithmId);
      createReference.setInt(2, bookId);
      logger.debug(() -> Util.format(createReference));
      createReference.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to create reference with algorithm_id = {} and book_id = {}",
          algorithmId,
          bookId);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public void deleteReference(int algorithmId, int bookId) throws SQLException {
    try {
      deleteReference.setInt(1, algorithmId);
      deleteReference.setInt(2, bookId);
      logger.debug(() -> Util.format(deleteReference));
      deleteReference.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error(
          "Failed to delete reference with algorithm_id = {} and book_id = {}",
          algorithmId,
          bookId);
      rollBack(connection);
      throw e;
    }
  }
}
