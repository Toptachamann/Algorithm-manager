package com.algorithm.manager.dao.interf;

import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.model.Reference;

import java.util.List;

public interface ReferenceDao {

  int persist(Reference reference) throws Exception;

  List<Reference> getReferences(Algorithm algorithm, Book book) throws Exception;

  default boolean containsReference(Algorithm algorithm, Book book) throws Exception {
    return !getReferences(algorithm, book).isEmpty();
  }

  List<Reference> getAlgorithmReferences(Algorithm algorithm) throws Exception;

  List<Reference> getReferencesInBook(Book book) throws Exception;

  void delete(Reference reference) throws Exception;

  void deleteById(int id) throws Exception;

  void deleteReferences(Algorithm algorithm, Book book) throws Exception;
}
