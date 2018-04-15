package com.algorithm.manager.dao.interf

import com.algorithm.manager.model.FieldOfStudy
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class FieldDaoImplTest extends Specification {
  @Shared
  def name = "Test field of study"
  @Shared
  def updated = "Updated test field of study"
  @Shared
  def description = "Test field description"
  @Shared
  def jdbcDao = new com.algorithm.manager.dao.jdbc.FieldDaoImpl()
  @Shared
  def hibernateDao = new com.algorithm.manager.dao.hibernate.FieldDaoImpl()

  @Unroll
  def "test field creation by name"() {
    when:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    then:
    fieldDao.containsFieldOfStudy(fieldOfStudy)
    fieldDao.containsFieldOfStudyWithName(name)
    fieldOfStudy.getField() == name
    fieldOfStudy.getDescription() == null
    def optField = fieldDao.getFieldByName(name)
    optField.isPresent()
    def field = optField.get()
    field == fieldOfStudy
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [jdbcDao, hibernateDao]
  }

  @Unroll
  def "test field creation by name and description"() {
    when:
    def fieldOfStudy = new FieldOfStudy(name, description);
    fieldDao.persist(fieldOfStudy)
    then:
    fieldOfStudy.getField() == name
    fieldOfStudy.getDescription() == description
    fieldDao.containsFieldOfStudyWithName(name)
    fieldDao.containsFieldOfStudy(fieldOfStudy)
    def optField = fieldDao.getFieldByName(name)
    optField.isPresent()
    def field = optField.get()
    field.getField() == name
    field.getDescription() == description
    field.getId() == fieldOfStudy.getId()
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [jdbcDao, hibernateDao]
  }

  @Unroll
  def "test field deletion"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    when:
    fieldDao.delete(fieldOfStudy)
    then:
    !fieldDao.containsFieldOfStudyWithName(name)
    where:
    fieldDao << [jdbcDao, hibernateDao]
  }

  @Unroll
  def "test field update"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    when:
    fieldOfStudy.setField(updated)
    fieldDao.merge(fieldOfStudy)
    then:
    !fieldDao.containsFieldOfStudyWithName(name)
    fieldDao.containsFieldOfStudyWithName(updated)
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [jdbcDao, hibernateDao]
  }
}
