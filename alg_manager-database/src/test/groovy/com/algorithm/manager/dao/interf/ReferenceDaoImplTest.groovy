package com.algorithm.manager.dao.interf

import com.algorithm.manager.dao.hibernate.*
import com.algorithm.manager.model.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ReferenceDaoImplTest extends Specification {
  @Shared
  def hibernateDao = new com.algorithm.manager.dao.hibernate.ReferenceDaoImpl()
  @Shared
  ParadigmDao paradigmDao
  @Shared
  FieldDao fieldDao
  @Shared
  AlgorithmDao algorithmDao
  @Shared
  AuthorDao authorDao
  @Shared
  BookDao bookDao
  @Shared
  DesignParadigm paradigm
  @Shared
  FieldOfStudy field
  @Shared
  Algorithm algorithm
  @Shared
  Author author1
  @Shared
  Author author2
  @Shared
  Book book

  def setupSpec() {
    paradigm = new DesignParadigm("Test design paradigm")
    field = new FieldOfStudy("Test field of study")
    algorithm = new Algorithm("Test algorithm", "Test complecity", paradigm, field)
    author1 = new Author("Test first name 1", "Test last name 1")
    author2 = new Author("Test last name 2", "Test last name 2")
    book = new Book("Test book title", null, 1, Arrays.asList(author1, author2))

    paradigmDao = new ParadigmDaoImpl()
    fieldDao = new FieldDaoImpl()
    algorithmDao = new AlgorithmDaoImpl()
    authorDao = new AuthorDaoImpl()
    bookDao = new BookDaoImpl()

    algorithmDao.deleteByName(algorithm.getName())
    paradigmDao.deleteByName(paradigm.getName())
    fieldDao.deleteByName(field.getName())
    authorDao.deleteByFullName(author1.getFirstName(), author1.getLastName())
    authorDao.deleteByFullName(author2.getFirstName(), author2.getLastName())
    bookDao.delete(book)

    paradigmDao.persist(paradigm)
    fieldDao.persist(field)
    algorithmDao.persist(algorithm)
    authorDao.persist(author1)
    authorDao.persist(author2)
    bookDao.persist(book)
  }

  def cleanupSpec() {
    algorithmDao.deleteByName(algorithm.getName())
    paradigmDao.delete(paradigm)
    fieldDao.delete(field)
    bookDao.delete(book)
    authorDao.deleteByFullName(author1.getFirstName(), author1.getLastName())
    authorDao.deleteByFullName(author2.getFirstName(), author2.getLastName())
  }

  @Unroll
  def "test reference creation"() {
    when:
    def reference = new Reference(algorithm, book)
    referenceDao.persist(reference)
    then:
    referenceDao.containsReference(algorithm, book)
    def list = referenceDao.getReferences(algorithm, book)
    !list.isEmpty()
    def created = list.get(0)
    created == reference
    def referenceList = referenceDao.getAlgorithmReferences(algorithm)
    referenceList.size() == 1
    referenceList.get(0) == reference
    cleanup:
    referenceDao.delete(reference)
    where:
    referenceDao << [hibernateDao]
  }

  @Unroll
  def "test reference deletion"() {
    setup:
    def reference = new Reference(algorithm, book)
    referenceDao.persist(reference)
    when:
    referenceDao.delete(reference)
    then:
    !referenceDao.containsReference(algorithm, book)
    def list = referenceDao.getReferences(algorithm, book)
    list.isEmpty()
    def referenceList = referenceDao.getAlgorithmReferences(algorithm)
    referenceList.size() == 0
    where:
    referenceDao << [hibernateDao]
  }

}
