package app.dao.interf

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ParadigmDaoImplTest extends Specification {
  @Shared
  def name = "test paradigm"
  @Shared
  def updatedName = "test updated paradigm"
  @Shared
  def description = "test description"

  @Unroll
  def "test paradigm creation"() {
    setup:
    def created = paradigmDao.createParadigm(name)
    expect:
    created.getParadigm() == name
    created.getDescription() == null
    paradigmDao.containsParadigm(created.getId())
    paradigmDao.containsParadigm(created.getParadigm())
    def optParadigm = paradigmDao.getParadigmByName(name)
    optParadigm.isPresent()
    def paradigm = optParadigm.get()
    paradigm == created
    cleanup:
    paradigmDao.deleteParadigm(created.getId())
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test paradigm creation with description"() {
    setup:
    def created = paradigmDao.createParadigm(name, description)
    expect:
    created.getParadigm() == name
    created.getDescription() == description
    paradigmDao.containsParadigm(created.getId())
    paradigmDao.containsParadigm(created.getParadigm())
    def optParadigm = paradigmDao.getParadigmByName(name)
    optParadigm.isPresent()
    def paradigm = optParadigm.get()
    paradigm == created
    cleanup:
    paradigmDao.deleteParadigm(created.getId())
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test paradigm deletion"() {
    setup:
    def created = paradigmDao.createParadigm(name)
    paradigmDao.deleteParadigm(created.getId())
    expect:
    !paradigmDao.containsParadigm(created.getId())
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test update design paradigm"() {
    setup:
    def created = paradigmDao.createParadigm(name)
    paradigmDao.updateDesignParadigm(updatedName, created.getId())
    expect:
    !paradigmDao.containsParadigm(name)
    paradigmDao.containsParadigm(updatedName)
    cleanup:
    paradigmDao.deleteParadigm(created.getId())
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

}
