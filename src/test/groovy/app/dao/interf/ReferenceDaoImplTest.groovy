package app.dao.interf

import app.model.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ReferenceDaoImplTest extends Specification {
  @Shared
  def jdbcDao = new app.dao.jdbc.ReferenceDaoImpl()
  @Shared
  def hibernateDao = new app.dao.hibernate.ReferenceDaoImpl()
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

    paradigmDao = new app.dao.jdbc.ParadigmDaoImpl()
    fieldDao = new app.dao.jdbc.FieldDaoImpl()
    algorithmDao = new app.dao.jdbc.AlgorithmDaoImpl()
    authorDao = new app.dao.jdbc.AuthorDaoImpl()
    bookDao = new app.dao.jdbc.BookDaoImpl()

    paradigmDao.persist(paradigm)
    fieldDao.persist(field)
    algorithmDao.persist(algorithm)
    authorDao.persist(author1)
    authorDao.persist(author2)
    bookDao.persist(book)
  }

  def cleanupSpec() {
    algorithmDao.delete(algorithm)
    paradigmDao.delete(paradigm)
    fieldDao.delete(field)
    bookDao.delete(book)
    authorDao.delete(author1)
    authorDao.delete(author2)
  }

  @Unroll
  def "test reference creation"() {
    when:
    def reference = new Reference(algorithm, book)
    referenceDao.persist(reference)
    then:
    referenceDao.containsReference(algorithm, book)
    def optCreated = referenceDao.getReference(algorithm, book)
    optCreated.isPresent()
    def created = optCreated.get()
    created == reference
    def referenceList = referenceDao.getAlgorithmReferences(algorithm)
    referenceList.size() == 1
    referenceList.get(0) == reference
    cleanup:
    referenceDao.delete(reference)
    where:
    referenceDao << [jdbcDao, hibernateDao]
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
    def optDeleted = referenceDao.getReference(algorithm, book)
    !optDeleted.isPresent()
    def referenceList = referenceDao.getAlgorithmReferences(algorithm)
    referenceList.size() == 0
    where:
    referenceDao << [jdbcDao, hibernateDao]
  }

}
