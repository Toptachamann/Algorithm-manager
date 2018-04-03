package app.dao.interf

import app.model.FieldOfStudy
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

  @Unroll
  def "test field creation by name"() {
    when:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    then:
    fieldDao.containsFieldOfStudy(fieldOfStudy.getId())
    fieldDao.containsFieldOfStudy(name)
    fieldOfStudy.getField() == name
    fieldOfStudy.getDescription() == null
    def optField = fieldDao.getFieldByName(name)
    optField.isPresent()
    def field = optField.get()
    field == fieldOfStudy
    cleanup:
    fieldDao.deleteFieldOfStudy(fieldOfStudy)
    where:
    fieldDao << [new app.dao.jdbc.FieldDaoImpl(), new app.dao.jdbc.FieldDaoImpl()]
  }

  @Unroll
  def "test field creation by name and description"() {
    when:
    def fieldOfStudy = new FieldOfStudy(name, description);
    fieldDao.persist(fieldOfStudy)
    then:
    fieldOfStudy.getField() == name
    fieldOfStudy.getDescription() == description
    fieldDao.containsFieldOfStudy(name)
    fieldDao.containsFieldOfStudy(fieldOfStudy.getId())
    def optField = fieldDao.getFieldByName(name)
    optField.isPresent()
    def field = optField.get()
    field.getField() == name
    field.getDescription() == description
    field.getId() == fieldOfStudy.getId()
    cleanup:
    fieldDao.deleteFieldOfStudy(fieldOfStudy)
    where:
    fieldDao << [new app.dao.jdbc.FieldDaoImpl(), new app.dao.jdbc.FieldDaoImpl()]
  }

  @Unroll
  def "test field deletion"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    when:
    fieldDao.deleteFieldOfStudy(fieldOfStudy)
    then:
    !fieldDao.containsFieldOfStudy(name)
    where:
    fieldDao << [new app.dao.jdbc.FieldDaoImpl(), new app.dao.jdbc.FieldDaoImpl()]
  }

  @Unroll
  def "test field update"() {
    setup:
    def fieldOfStudy = new FieldOfStudy(name)
    fieldDao.persist(fieldOfStudy)
    when:
    fieldDao.setField(fieldOfStudy, updated)
    then:
    !fieldDao.containsFieldOfStudy(name)
    fieldDao.containsFieldOfStudy(updated)
    cleanup:
    fieldDao.deleteFieldOfStudy(fieldOfStudy)
    where:
    fieldDao << [new app.dao.jdbc.FieldDaoImpl(), new app.dao.jdbc.FieldDaoImpl()]
  }
}
