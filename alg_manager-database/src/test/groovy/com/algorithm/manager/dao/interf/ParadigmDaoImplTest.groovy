package com.algorithm.manager.dao.interf

import com.algorithm.manager.model.DesignParadigm
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ParadigmDaoImplTest extends Specification {
  @Shared
  def name = "test paradigm"
  @Shared
  def updatedName = "test updatedName paradigm"
  @Shared
  def description = "test description"
  @Shared
  def updatedDescription = "test updatedName description"
  @Shared
  def hibernateDao = new com.algorithm.manager.dao.hibernate.ParadigmDaoImpl()

  def setup(){
    hibernateDao.deleteByName(name)
    hibernateDao.deleteByName(updatedName)
  }

  @Unroll
  def "test persist by name"() {
    setup:
    def paradigm = paradigmDao.persist(name)
    expect:
    paradigmDao.containsParadigmWithId(paradigm.getId())
    cleanup:
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test persist by name and description"() {
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
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test contains with id"() {
    setup:
    def paradigm = new DesignParadigm(name)
    int id = paradigmDao.persist(paradigm)
    expect:
    paradigmDao.containsParadigmWithId(id)
    cleanup:
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test contains with name"() {
    setup:
    def paradigm = paradigmDao.persist(name)
    expect:
    paradigmDao.containsParadigmWithName(name)
    cleanup:
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test get by id"() {
    setup:
    def paradigm = paradigmDao.persist(name, description)
    def optCreated = paradigmDao.getParadigmById(paradigm.getId())
    expect:
    optCreated.isPresent()
    def created = optCreated.get()
    created == paradigm
    cleanup:
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test get by name"() {
    setup:
    def paradigm = paradigmDao.persist(name, description)
    def optCreated = paradigmDao.getDesignParadigmByName(paradigm.getName())
    expect:
    optCreated.isPresent()
    def created = optCreated.get()
    created == paradigm
    cleanup:
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test set name"() {
    setup:
    def paradigm = new DesignParadigm(name)
    int id = paradigmDao.persist(paradigm)
    when:
    paradigmDao.setName(id, updatedName)
    paradigmDao.refresh(paradigm)
    then:
    paradigm.getName() == updatedName
    cleanup:
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test set description"() {
    setup:
    def paradigm = new DesignParadigm(name)
    int id = paradigmDao.persist(paradigm)
    when:
    paradigmDao.setDescription(id, updatedDescription)
    paradigmDao.refresh(paradigm)
    then:
    paradigm.getDescription() == updatedDescription
    cleanup:
    paradigmDao.delete(paradigm)
    where:
    paradigmDao << [hibernateDao]
  }

  @Unroll
  def "test merge"() {
    setup:
    def paradigm = new DesignParadigm(name)
    paradigmDao.persist(paradigm)
    paradigm.setName(updatedName)
    paradigm.setDescription(updatedDescription)
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
  def "test delete"() {
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

  @Unroll
  def "test delete by id"() {
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

  @Unroll
  def "test delete by name"() {
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
