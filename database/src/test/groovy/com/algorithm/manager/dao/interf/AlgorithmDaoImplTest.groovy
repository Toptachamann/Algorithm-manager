package com.algorithm.manager.dao.interf

import com.algorithm.manager.dao.hibernate.FieldDaoImpl
import com.algorithm.manager.dao.hibernate.ParadigmDaoImpl
import com.algorithm.manager.model.Algorithm
import com.algorithm.manager.model.DesignParadigm
import com.algorithm.manager.model.FieldOfStudy
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class AlgorithmDaoImplTest extends Specification {
  @Shared
  DesignParadigm paradigm
  @Shared
  FieldOfStudy field
  @Shared
  Algorithm algorithm
  @Shared
  def paradigmDao = new ParadigmDaoImpl()
  @Shared
  def fieldDao = new FieldDaoImpl()
  @Shared
  def jdbcDao = new com.algorithm.manager.dao.jdbc.AlgorithmDaoImpl()
  @Shared
  def hibernateDao = new com.algorithm.manager.dao.hibernate.AlgorithmDaoImpl()

  def setup() {
    paradigm = new com.algorithm.manager.model.DesignParadigm("Test design paradigm")
    field = new FieldOfStudy("Test field of study")
    algorithm = new Algorithm("test name", "test complexity", paradigm, field)
    paradigmDao.persist(paradigm)
    fieldDao.persist(field)
  }

  def cleanup() {
    paradigmDao.delete(paradigm)
    fieldDao.delete(field)
  }

  @Unroll
  def "test algorithm creation"() {
    setup:
    algorithmDao.persist(algorithm)
    expect:
    algorithmDao.containsAlgorithm(algorithm.getName())
    def optCreated = algorithmDao.getAlgorithmByName(algorithm.getName())
    optCreated.isPresent()
    def created = optCreated.get()
    created == algorithm
    cleanup:
    algorithmDao.deleteById(algorithm)
    where:
    algorithmDao << [hibernateDao, jdbcDao]
  }

  @Unroll
  def "test algorithm deletion"() {
    setup:
    algorithmDao.persist(algorithm)
    algorithmDao.deleteById(algorithm)
    expect:
    !algorithmDao.containsAlgorithm(algorithm.getName())
    where:
    algorithmDao << [hibernateDao, jdbcDao]
  }

  @Unroll
  def "test set name"() {
    setup:
    def updatedParadigm = new DesignParadigm("Test updated design paradigm")
    def updatedField = new FieldOfStudy("Test updated field of study")
    paradigmDao.persist(updatedParadigm)
    fieldDao.persist(updatedField)

    algorithmDao.persist(algorithm)
    def oldName = algorithm.getName()
    algorithm.setName("Test updated name")
    algorithm.setComplexity("Test updatedComplexity")
    algorithm.setDesignParadigm(updatedParadigm)
    algorithm.setFieldOfStudy(updatedField)
    algorithmDao.merge(algorithm)
    expect:
    !algorithmDao.containsAlgorithm(oldName)
    algorithmDao.containsAlgorithm("Test updated name")
    def optCreated = algorithmDao.getAlgorithmByName("Test updated name")
    optCreated.isPresent()
    def created = optCreated.get()
    created == algorithm
    cleanup:
    algorithmDao.deleteById(algorithm)
    paradigmDao.delete(updatedParadigm)
    fieldDao.delete(updatedField)
    where:
    algorithmDao << [hibernateDao, jdbcDao]
  }
}
