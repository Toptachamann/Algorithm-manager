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
  def hibernateDao = new com.algorithm.manager.dao.hibernate.AuthorDaoImpl()

  @Unroll
  def "test author creation"() {
    given:
    def author = new Author(firstName, lastName)
    authorDao.persist(author)
    expect:
    authorDao.containsAuthorWithFullName(firstName, lastName)
    def optAuthor = authorDao.getByFullName(firstName, lastName)
    optAuthor.isPresent()
    optAuthor.get() == author
    cleanup:
    authorDao.delete(author)
    where:
    authorDao << [hibernateDao]
  }

  @Unroll
  def "test author deletion"() {
    given:
    def author = new Author(firstName, lastName)
    authorDao.persist(author)
    expect:
    authorDao.containsAuthorWithFullName(firstName, lastName)
    authorDao.delete(author)
    !authorDao.containsAuthorWithFullName(firstName, lastName)
    !authorDao.getByFullName(firstName, lastName).isPresent()
    where:
    authorDao << [hibernateDao]
  }
}
