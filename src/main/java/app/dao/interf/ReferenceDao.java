package app.dao.interf;

import app.model.Algorithm;
import app.model.Book;
import app.model.Reference;

import java.util.List;
import java.util.Optional;

public interface ReferenceDao {

  void persist(Reference reference) throws Exception;

  Optional<Reference> getReference(Algorithm algorithm, Book book) throws Exception;

  default boolean containsReference(Algorithm algorithm, Book book) throws Exception {
    return getReference(algorithm, book).isPresent();
  }

  List<Reference> getAlgorithmReferences(Algorithm algorithm) throws Exception;

  void deleteReference(Reference reference) throws Exception;
}
