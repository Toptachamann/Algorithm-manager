package com.algorithm.manager.app.service;

import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Author;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.model.Reference;

import java.util.List;

public interface BookService {

  Book createBook(String title, Integer volumeStr, Integer editionStr, List<Author> authors)
      throws Exception;

  List<Book> getAllBooks() throws Exception;

  List<Book> searchBooks(String title, Integer editionStr, Integer volumeStr, List<Author> authors)
      throws Exception;

  void deleteBook(Book book) throws Exception;

  Author createAuthor(String firstName, String lastName) throws Exception;

  void updateTitle(Book book, String title) throws Exception;

  void updateVolume(Book book, Integer volume) throws Exception;

  void updateEdition(Book book, Integer edition) throws Exception;

  List<Author> setAuthors(Book book, List<Author> authors) throws Exception;

  Reference createReference(Algorithm algorithm, Book book) throws Exception;

  List<Reference> getReferences(Algorithm algorithm) throws Exception;

  void deleteReference(Algorithm algorithm, Book book) throws Exception;
}
