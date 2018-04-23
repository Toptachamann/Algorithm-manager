package com.algorithm.manager.dao.interf

import com.algorithm.manager.model.DesignParadigm
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
  @Shared
  def hibernateDao = new com.algorithm.manager.dao.hibernate.ParadigmDaoImpl()

  @Unroll
  def "test paradigm creation"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.persist(paradigm)
    def optCreatedById = paradigmDao.getParadigmById(paradigm.getId())
    def optCreatedByName = paradigmDao.getParadigmByName(paradigm.getName())
    expect:
    optCreatedById.isPresent()
    optCreatedByName.isPresent()
    def createdById = optCreatedById.get()
    def createdByName = optCreatedByName.get()
    createdById == paradigm
    createdByName == paradigm
    paradigmDao.containsParadigmWithId(createdById.getId())
    paradigmDao.containsParadigmWithName(createdById.getName())
    cleanup:
    paradigmDao.delete(createdById)
    where:
    paradigmDao << [hibernateDao]
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
    created.getName() == name
    created.getDescription() == description
    paradigmDao.containsParadigmWithName(created.getName())
    paradigmDao.containsParadigmWithName(created.getName())
    paradigm == created
    cleanup:
    paradigmDao.delete(created)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test merge design paradigm"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.persist(paradigm)
    paradigm.setName(updatedName)
    paradigmDao.merge(paradigm)
    expect:
    !paradigmDao.containsParadigmWithName(name)
    paradigmDao.containsParadigmWithName(updatedName)
    cleanup:
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test paradigm deletion"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.persist(paradigm)
    paradigmDao.delete(paradigm)
    expect:
    !paradigmDao.containsParadigmWithId(paradigm.getId())
    !paradigmDao.containsParadigmWithName(paradigm.getName())
    where:
    paradigmDao << [hibernateDao]
  }

  def "test paradigm deletion by id"() {
    setup:
    def paradigm = new DesignParadigm(name)
    int id = paradigmDao.persist(paradigm)
    paradigmDao.deleteById(id)
    expect:
    !paradigmDao.containsParadigmWithId(paradigm.getId())
    !paradigmDao.containsParadigmWithName(paradigm.getName())
    where:
    paradigmDao << [hibernateDao]
  }

  def "test paradigm deletion by name"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.persist(paradigm)
    paradigmDao.deleteByName(paradigm.getName())
    expect:
    !paradigmDao.containsParadigmWithId(paradigm.getId())
    !paradigmDao.containsParadigmWithName(paradigm.getName())
    where:
    paradigmDao << [hibernateDao]
  }

}
