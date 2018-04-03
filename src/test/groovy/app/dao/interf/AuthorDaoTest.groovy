package app.dao.interf

import app.model.Author
import spock.lang.Shared
import spock.lang.Specification

class AuthorDaoTest extends Specification {
  @Shared
  def firstName = "Test first name"
  @Shared
  def lastName = "Test last name"
  @Shared
  def jdbcDao = new app.dao.jdbc.AuthorDaoImpl()
  @Shared
  def hibernateDao = new app.dao.hibernate.AuthorDaoImpl()


  def "test author creation"() {
    given:
    def author = new Author(firstName, lastName)
    authorDao.persist(author)
    expect:
    authorDao.containsAuthor(firstName, lastName)
    def optAuthor = authorDao.getAuthor(firstName, lastName)
    optAuthor.isPresent()
    optAuthor.get() == author
    cleanup:
    authorDao.deleteAuthor(author)
    where:
    authorDao << [jdbcDao, hibernateDao]
  }

  def "test author deletion"(){
    given:
    def author = new Author(firstName, lastName)
    authorDao.persist(author)
    expect:
    authorDao.containsAuthor(firstName, lastName)
    authorDao.deleteAuthor(author)
    !authorDao.containsAuthor(firstName, lastName)
    !authorDao.getAuthor(firstName, lastName).isPresent()
    where:
    authorDao << [jdbcDao, hibernateDao]
  }
}
