package com.algorithm.manager.dao.interf

import com.algorithm.manager.model.FieldOfStudy
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class FieldDaoImplTest extends Specification {
  @Shared
  def name = "Test field of study"
  @Shared
  def description = "Test field description"
  @Shared
  def updatedName = "Updated test field of study"
  @Shared
  def updatedDescription = "Test updated description"
  @Shared
  def hibernateDao = new com.algorithm.manager.dao.hibernate.FieldDaoImpl()

  @Unroll
  def "test persist by name"() {
    when:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    then:
    fieldDao.containsFieldOfStudyWithId(fieldOfStudy.getId())
    fieldDao.containsFieldOfStudyWithName(fieldOfStudy.getName())
    fieldDao.containsFieldOfStudyWithName(name)
    fieldOfStudy.getName() == name
    fieldOfStudy.getDescription() == null
    def optField = fieldDao.getFieldOfStudyByName(name)
    optField.isPresent()
    def field = optField.get()
    field == fieldOfStudy
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  @Unroll
  def "test persist by name and description"() {
    when:
    def fieldOfStudy = new FieldOfStudy(name, description)
    fieldDao.persist(fieldOfStudy)
    then:
    fieldOfStudy.getName() == name
    fieldOfStudy.getDescription() == description
    fieldDao.containsFieldOfStudyWithName(name)
    fieldDao.containsFieldOfStudyWithName(fieldOfStudy.getName())
    def optField = fieldDao.getFieldOfStudyByName(name)
    optField.isPresent()
    def field = optField.get()
    field.getName() == name
    field.getDescription() == description
    field.getId() == fieldOfStudy.getId()
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  @Unroll
  def "test merge"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    when:
    fieldOfStudy.setName(updatedName)
    fieldDao.merge(fieldOfStudy)
    then:
    !fieldDao.containsFieldOfStudyWithName(name)
    fieldDao.containsFieldOfStudyWithName(updatedName)
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  def "test contains with id"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    int id = fieldDao.persist(fieldOfStudy)
    expect:
    fieldDao.containsFieldOfStudyWithId(id)
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  def "test contains with name"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    expect:
    fieldDao.containsFieldOfStudyWithName(name)
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  def "test get by id"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    int id = fieldDao.persist(fieldOfStudy)
    expect:
    def optCreated = fieldDao.getFieldOfStudyById(id)
    optCreated.isPresent()
    def created = optCreated.get()
    created == fieldOfStudy
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  def "test get by name"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    expect:
    def optCreated = fieldDao.getFieldOfStudyByName(name)
    optCreated.isPresent()
    def created = optCreated.get()
    created == fieldOfStudy
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  @Unroll
  def "test set name"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    int id = fieldDao.persist(fieldOfStudy)
    when:
    fieldDao.setName(id, updatedName)
    then:
    !fieldDao.containsFieldOfStudyWithName(name)
    fieldDao.containsFieldOfStudyWithName(updatedName)
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  @Unroll
  def "test set description"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    int id = fieldDao.persist(fieldOfStudy)
    when:
    fieldDao.setDescription(id, updatedDescription)
    fieldDao.refresh(fieldOfStudy)
    then:
    fieldOfStudy.getDescription() == updatedDescription
    cleanup:
    fieldDao.delete(fieldOfStudy)
    where:
    fieldDao << [hibernateDao]
  }

  @Unroll
  def "test delete"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    when:
    fieldDao.delete(fieldOfStudy)
    then:
    !fieldDao.containsFieldOfStudyWithName(name)
    where:
    fieldDao << [hibernateDao]
  }

  @Unroll
  def "test delete by id"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    int id = fieldDao.persist(fieldOfStudy)
    when:
    fieldDao.deleteById(id)
    then:
    !fieldDao.containsFieldOfStudyWithId(id)
    where:
    fieldDao << [hibernateDao]
  }

  @Unroll
  def "test delete by name"() {
    setup:
    fieldDao.persist(name)
    when:
    fieldDao.deleteByName(name)
    then:
    !fieldDao.containsFieldOfStudyWithName(name)
    where:
    fieldDao << [hibernateDao]
  }
}
