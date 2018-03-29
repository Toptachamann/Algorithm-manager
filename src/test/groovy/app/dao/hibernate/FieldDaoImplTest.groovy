package app.dao.hibernate

import spock.lang.Shared
import spock.lang.Specification

class FieldDaoImplTest extends Specification {
  @Shared
  def fieldDao = new FieldDaoImpl()
  @Shared
  def name = "Test field of study"
  @Shared
  def updated = "Updated test field of study"
  @Shared
  def description = "Test field description"

  def "test field creation by name"(){
    when:
    def created = fieldDao.createFieldOfStudy(name)
    then:
    fieldDao.containsFieldOfStudy(created.getId())
    fieldDao.containsFieldOfStudy(name)
    created.getField() == name
    created.getDescription() == null
    def optField = fieldDao.getFieldByName(name)
    optField.isPresent()
    def field = optField.get()
    field == created
    cleanup:
    fieldDao.deleteFieldOfStudy(created.getId())
  }

  def "test field creation by name and description"(){
    when:
    def created = fieldDao.createFieldOfStudy(name, description)
    then:
    created.getField() == name
    created.getDescription() == description
    fieldDao.containsFieldOfStudy(name)
    fieldDao.containsFieldOfStudy(created.getId())
    def optField = fieldDao.getFieldByName(name)
    optField.isPresent()
    def field = optField.get()
    field.getField() == name
    field.getDescription() == description
    field.getId() == created.getId()
    cleanup:
    fieldDao.deleteFieldOfStudy(field.getId())
  }

  def "test field deletion"(){
    setup:
    def created = fieldDao.createFieldOfStudy(name)
    when:
    fieldDao.deleteFieldOfStudy(created.getId())
    then:
    !fieldDao.containsFieldOfStudy(name)
  }

  def "test field update"(){
    setup:
    def created = fieldDao.createFieldOfStudy(name)
    when:
    fieldDao.updateFieldOfStudy( updated, created.getId())
    then:
    !fieldDao.containsFieldOfStudy(name)
    fieldDao.containsFieldOfStudy(updated)
    cleanup:
    fieldDao.deleteFieldOfStudy(created.getId())
  }
}
