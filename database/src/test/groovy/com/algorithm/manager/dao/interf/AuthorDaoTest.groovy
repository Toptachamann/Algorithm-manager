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
    def jdbcDao = new com.algorithm.manager.dao.jdbc.AuthorDaoImpl()
    @Shared
    def hibernateDao = new com.algorithm.manager.dao.hibernate.AuthorDaoImpl()

    @Unroll
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
        authorDao.delete(author)
        where:
        authorDao << [jdbcDao, hibernateDao]
    }

    @Unroll
    def "test author deletion"() {
        given:
        def author = new Author(firstName, lastName)
        authorDao.persist(author)
        expect:
        authorDao.containsAuthor(firstName, lastName)
        authorDao.delete(author)
        !authorDao.containsAuthor(firstName, lastName)
        !authorDao.getAuthor(firstName, lastName).isPresent()
        where:
        authorDao << [jdbcDao, hibernateDao]
    }
}
