package com.algorithm.manager.dao.interf

import com.algorithm.manager.dao.hibernate.AuthorDaoImpl
import com.algorithm.manager.model.Author
import com.algorithm.manager.model.Book
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class BookDaoImplTest extends Specification {
    @Shared
    def authorDao = new AuthorDaoImpl()
    @Shared
    def jdbcDao = new com.algorithm.manager.dao.jdbc.BookDaoImpl()
    @Shared
    def hibernateDao = new com.algorithm.manager.dao.hibernate.BookDaoImpl()
    @Shared
    List<Author> authors

    def setup() {
        def author1 = new Author("test first name 1", "test last name 1")
        def author2 = new Author("test first name 2", "test last name 2")
        authors = [author1, author2]
        for (Author author : authors) {
            authorDao.persist(author)
        }
    }

    def cleanup() {
        for (Author author : authors) {
            authorDao.delete(author)
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
        bookDao.delete(book)
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
