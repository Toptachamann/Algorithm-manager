package app.dao.interf

import app.model.Author
import app.model.Book
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class BookDaoImplTest extends Specification {
  @Shared
  def authorDao = new app.dao.jdbc.AuthorDaoImpl()
  @Shared
  def jdbcDao = new app.dao.jdbc.BookDaoImpl()
  @Shared
  def hibernateDao = new app.dao.hibernate.BookDaoImpl()
  @Shared
  List<Author> authors

  def setup() {
    def author1 = new Author("test first name 1", "test last name 1")
    def author2 = new Author("test first name 2", "test last name 2")
    authors = [author1, author2]
    for(Author author : authors){
      authorDao.persist(author)
    }
  }

  def cleanup() {
    for(Author author : authors){
      authorDao.deleteAuthor(author)
    }
  }

  @Unroll
  def "test book creation"() {
    setup:
    def book = new Book("Test book title", volume, 1, authors)
    bookDao.persist(book)
    expect:
    bookDao.containsBook(book)
    def optCreated = bookDao.getBookById(book.getId())
    optCreated.isPresent()
    def created = optCreated.get()
    created == book
    cleanup:
    bookDao.deleteBook(book)
    where:
    bookDao      | volume
    jdbcDao      | null
    jdbcDao      | 1
    hibernateDao | null
    hibernateDao | 1
  }

  /*def "test book deletion"() {
    where:
    bookDao << [hibernateDao, jdbcDao]
  }*/
}
