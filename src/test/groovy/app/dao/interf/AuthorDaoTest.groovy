package app.dao.interf

import spock.lang.Shared
import spock.lang.Specification

class AuthorDaoTest extends Specification {
  @Shared
  def firstName = "Test first name"
  @Shared
  def lastName = "Test last name"


  def "test author creation"() {
    given:
    def created = authorDao.createAuthor(firstName, lastName)
    expect:
    authorDao.containsAuthor(firstName, lastName)
    def optAuthor = authorDao.getAuthor(firstName, lastName)
    optAuthor.isPresent()
    optAuthor.get() == created
    cleanup:
    authorDao.deleteAuthor(created)
    where:
    authorDao << [new app.dao.jdbc.AuthorDaoImpl(), new app.dao.hibernate.AuthorDaoImpl()]
  }

  def "test author deletion"(){
    given:
    def created = authorDao.createAuthor(firstName, lastName)
    expect:
    authorDao.containsAuthor(firstName, lastName)
    authorDao.deleteAuthor(created)
    !authorDao.containsAuthor(firstName, lastName)
    !authorDao.getAuthor(firstName, lastName).isPresent()
    where:
    authorDao << [new app.dao.jdbc.AuthorDaoImpl(), new app.dao.hibernate.AuthorDaoImpl()]
  }
}
