package app.dao.jdbc;

import app.auxiliary.Util;
import app.dao.interf.ReferenceDao;
import app.model.Algorithm;
import app.model.Author;
import app.model.Book;
import app.model.Reference;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReferenceDaoImpl extends AbstractDao implements ReferenceDao {
  private static final Logger logger = LogManager.getLogger(ReferenceDaoImpl.class);

  private PreparedStatement countReferences;
  private PreparedStatement createReference;
  private PreparedStatement deleteReference;
  private PreparedStatement getReference;
  private PreparedStatement getAlgorithmReferences;

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
    getReference =
        connection.prepareStatement(
            "SELECT reference_id FROM algorithm_reference WHERE ref_algorithm_id = ? AND ref_book_id = ?");
    getAlgorithmReferences =
        connection.prepareStatement(
            "SELECT book_id, title, volume, edition, author_id, first_name, last_name,algos.ref_book_id\n"
                + "FROM (SELECT reference_id, ref_book_id FROM algorithm_reference WHERE ref_algorithm_id = ?) AS algos\n"
                + "INNER JOIN book ON algos.ref_book_id = book_id INNER JOIN textbook ON book_id = txtbk_book_id \n"
                + "INNER JOIN author ON txtbk_author_id = author_id\n"
                + "ORDER BY book_id, author_id");
  }

  @Override
  public void persist(Reference reference) throws SQLException {
    try {
      createReference.setInt(1, reference.getAlgorithm().getId());
      createReference.setInt(2, reference.getBook().getId());
      logger.debug(() -> Util.format(createReference));
      createReference.executeUpdate();
      reference.setReferenceId(getLastId(connection));
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to persist reference {}", reference);
      rollBack(connection);
      throw e;
    }
  }

  @Override
  public Optional<Reference> getReference(Algorithm algorithm, Book book) throws SQLException {
    getReference.setInt(1, algorithm.getId());
    getReference.setInt(2, book.getId());
    logger.debug(() -> Util.format(getReference));
    ResultSet set = getReference.executeQuery();
    return set.next()
        ? Optional.of(new Reference(set.getInt(1), algorithm, book))
        : Optional.empty();
  }

  @Override
  public List<Reference> getAlgorithmReferences(Algorithm algorithm) throws SQLException {
    getAlgorithmReferences.setInt(1, algorithm.getId());
    logger.debug(() -> Util.format(getAlgorithmReferences));
    ResultSet set = getAlgorithmReferences.executeQuery();
    return referencesFromResultSet(set, algorithm);
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
  public void deleteReference(Reference reference) throws SQLException {
    try {
      deleteReference.setInt(1, reference.getAlgorithm().getId());
      deleteReference.setInt(2, reference.getBook().getId());
      logger.debug(() -> Util.format(deleteReference));
      deleteReference.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      logger.catching(Level.ERROR, e);
      logger.error("Failed to delete reference {}", reference);
      rollBack(connection);
      throw e;
    }
  }

  private List<Reference> referencesFromResultSet(ResultSet set, Algorithm algorithm)
      throws SQLException {
    List<Reference> books = new ArrayList<>();
    if (set.next()) {
      for (; !set.isAfterLast(); ) {
        int volume = set.getInt(3);
        Book book;
        if (set.wasNull()) {
          book = new Book(set.getInt(1), set.getString(2), null, set.getInt(4));
        } else {
          book = new Book(set.getInt(1), set.getString(2), volume, set.getInt(4));
        }
        int id = set.getInt(1);
        do {
          book.addAuthor(new Author(set.getInt(5), set.getString(6), set.getString(7)));
        } while (set.next() && set.getInt(1) == id);
        books.add(new Reference(algorithm, book));
      }
    }
    return books;
  }
}
