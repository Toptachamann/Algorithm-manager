package app.dao.interf

import app.model.DesignParadigm
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
    def paradigm = new DesignParadigm(name)
    paradigmDao.persist(paradigm)
    def optCreated = paradigmDao.getParadigmById(paradigm.getId())
    expect:
    optCreated.isPresent()
    def created = optCreated.get()
    created.getParadigm() == name
    created.getDescription() == null
    paradigmDao.containsParadigm(created)
    paradigmDao.containsParadigm(created.getParadigm())
    paradigm == created
    cleanup:
    paradigmDao.deleteDesignParadigm(created)
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test paradigm creation with description"() {
    setup:
    def paradigm = new DesignParadigm(name, description)
    paradigmDao.persist(paradigm)
    def optCreated = paradigmDao.getParadigmById(paradigm.getId())
    expect:
    optCreated.isPresent()
    def created = optCreated.get()
    created.getParadigm() == name
    created.getDescription() == description
    paradigmDao.containsParadigm(created)
    paradigmDao.containsParadigm(created.getParadigm())
    paradigm == created
    cleanup:
    paradigmDao.deleteDesignParadigm(created)
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test paradigm deletion"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.persist(paradigm)
    paradigmDao.deleteDesignParadigm(paradigm)
    expect:
    !paradigmDao.containsParadigm(paradigm)
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test update design paradigm"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.persist(paradigm)
    paradigmDao.setParadigm(paradigm, updatedName)
    expect:
    !paradigmDao.containsParadigm(name)
    paradigmDao.containsParadigm(updatedName)
    cleanup:
    paradigmDao.deleteDesignParadigm(paradigm)
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

}
