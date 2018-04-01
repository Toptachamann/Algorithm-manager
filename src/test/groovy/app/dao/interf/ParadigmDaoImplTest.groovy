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
    paradigmDao.create(paradigm)
    def optCreated = paradigmDao.findById(paradigm.getId())
    expect:
    optCreated.isPresent()
    def created = optCreated.get()
    created.getParadigm() == name
    created.getDescription() == null
    paradigmDao.containsParadigm(created.getId())
    paradigmDao.containsParadigm(created.getParadigm())
    paradigm == created
    cleanup:
    paradigmDao.deleteDesignParadigmById(created.getId())
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test paradigm creation with description"() {
    setup:
    def paradigm = new DesignParadigm(name, description)
    paradigmDao.create(paradigm)
    def optCreated = paradigmDao.findById(paradigm.getId())
    expect:
    optCreated.isPresent()
    def created = optCreated.get()
    created.getParadigm() == name
    created.getDescription() == description
    paradigmDao.containsParadigm(created.getId())
    paradigmDao.containsParadigm(created.getParadigm())
    paradigm == created
    cleanup:
    paradigmDao.deleteDesignParadigmById(created.getId())
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test paradigm deletion"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.create(paradigm)
    paradigmDao.deleteDesignParadigmById(paradigm.getId())
    expect:
    !paradigmDao.containsParadigm(paradigm.getId())
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

  @Unroll
  def "test update design paradigm"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.create(paradigm)
    paradigmDao.updateDesignParadigm(updatedName, paradigm.getId())
    expect:
    !paradigmDao.containsParadigm(name)
    paradigmDao.containsParadigm(updatedName)
    cleanup:
    paradigmDao.deleteDesignParadigmById(paradigm.getId())
    where:
    paradigmDao << [new app.dao.hibernate.ParadigmDaoImpl(), new app.dao.jdbc.ParadigmDaoImpl()]
  }

}
