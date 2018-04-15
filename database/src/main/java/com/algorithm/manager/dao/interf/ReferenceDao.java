package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.model.Reference;

import java.util.List;
import java.util.Optional;

public interface ReferenceDao {

  void persist(Reference reference) throws Exception;

  Optional<Reference> getReference(Algorithm algorithm, Book book) throws Exception;

  default boolean containsReference(Algorithm algorithm, Book book) throws Exception {
    return getReference(algorithm, book).isPresent();
  }

  List<Reference> getAlgorithmReferences(Algorithm algorithm) throws Exception;

  void delete(Reference reference) throws Exception;
}
