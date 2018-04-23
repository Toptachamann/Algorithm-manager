package com.algorithm.manager.dao.interf

import com.algorithm.manager.model.Author
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class AuthorDaoTest extends Specification {
  @Shared
  def firstName = "Test first name"
  @Shared
  def lastName = "Test last name"
  @Shared
  def updatedFirstName = "Test updated first name"
  @Shared
  def updatedLastName = "Test updated last name"
  @Shared
  def hibernateDao = new com.algorithm.manager.dao.hibernate.AuthorDaoImpl()

  def setup() {
    hibernateDao.deleteByFullName(firstName, lastName)
  }

  @Unroll
  def "test persist and get by id"() {
    given:
    def author = new Author(firstName, lastName)
    int id = authorDao.persist(author)
    expect:
    authorDao.getById(id).get() == author
    cleanup:
    authorDao.delete(author)
    where:
    authorDao << [hibernateDao]
  }

  def "test refresh"() {
    given:
    def author = new Author(firstName, lastName)
    int id = authorDao.persist(author)
    when:
    authorDao.setFullName(id, updatedFirstName, updatedLastName)
    authorDao.refresh(author)
    then:
    author.getFirstName() == updatedFirstName
    author.getLastName() == updatedLastName
    cleanup:
    authorDao.delete(author)
    where:
    authorDao << [hibernateDao]
  }


  @Unroll
  def "test contains with id"() {
    given:
    def author = new Author(firstName, lastName)
    int id = authorDao.persist(author)
    expect:
    authorDao.containsAuthorWithId(id)
    cleanup:
    authorDao.delete(author)
    where:
    authorDao << [hibernateDao]
  }

  @Unroll
  def "test contains with full name"() {
    given:
    def author = new Author(firstName, lastName)
    authorDao.persist(author)
    expect:
    authorDao.containsAuthorWithFullName(firstName, lastName)
    cleanup:
    authorDao.delete(author)
    where:
    authorDao << [hibernateDao]
  }

  @Unroll
  def "test merge"() {
    setup:
    def author = new Author(firstName, lastName)
    authorDao.persist(author)
    when:
    author.setFirstName(updatedFirstName)
    author.setLastName(updatedLastName)
    authorDao.merge(author)
    then:
    !authorDao.containsAuthorWithFullName(firstName, lastName)
    authorDao.containsAuthorWithFullName(updatedFirstName, updatedLastName)
    cleanup:
    authorDao.delete(author)
    where:
    authorDao << [hibernateDao]
  }

  @Unroll
  def "test set full name"() {
    given:
    def author = new Author(firstName, lastName)
    int id = authorDao.persist(author)
    when:
    authorDao.setFullName(id, updatedFirstName, updatedLastName)
    then:
    !authorDao.containsAuthorWithFullName(firstName, lastName)
    authorDao.containsAuthorWithFullName(updatedFirstName, updatedLastName)
    cleanup:
    authorDao.delete(author)
    where:
    authorDao << [hibernateDao]
  }

  @Unroll
  def "test delete by id"() {
    given:
    def author = new Author(firstName, lastName)
    int id = authorDao.persist(author)
    when:
    authorDao.deleteById(id)
    then:
    !authorDao.containsAuthorWithId(id)
    where:
    authorDao << [hibernateDao]
  }

  @Unroll
  def "test delete"() {
    given:
    def author = new Author(firstName, lastName)
    int id = authorDao.persist(author)
    when:
    authorDao.delete(author)
    then:
    !authorDao.containsAuthorWithId(id)
    where:
    authorDao << [hibernateDao]
  }

  @Unroll
  def "test delete by full name"() {
    given:
    def author = new Author(firstName, lastName)
    int id = authorDao.persist(author)
    when:
    authorDao.deleteByFullName(firstName, lastName)
    then:
    !authorDao.containsAuthorWithId(id)
    where:
    authorDao << [hibernateDao]
  }
}
