package app.dao.hibernate

import spock.lang.Shared
import spock.lang.Specification

class ParadigmDaoImplTest extends Specification {
  @Shared
  def paradigmDao = new ParadigmDaoImpl()
  @Shared
  def name = "test paradigm"
  @Shared
  def updatedName = "test updated paradigm"
  @Shared
  def description = "test description"

  def "test paradigm creation"() {
    when:
    def created = paradigmDao.createParadigm(name)
    then:
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
  }

  def "test paradigm creation with description"() {
    when:
    def created = paradigmDao.createParadigm(name, description)
    then:
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
  }

  def "test paradigm deletion"() {
    setup:
    def created = paradigmDao.createParadigm(name)
    when:
    paradigmDao.deleteParadigm(created.getId())
    then:
    !paradigmDao.containsParadigm(created.getId())
  }

  def "test update design paradigm"() {
    setup:
    def created = paradigmDao.createParadigm(name)
    when:
    paradigmDao.updateDesignParadigm(updatedName, created.getId())
    then:
    !paradigmDao.containsParadigm(name)
    paradigmDao.containsParadigm(updatedName)
    cleanup:
    paradigmDao.deleteParadigm(created.getId())
  }

}
