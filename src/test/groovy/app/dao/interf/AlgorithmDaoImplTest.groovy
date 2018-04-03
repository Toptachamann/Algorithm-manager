package app.dao.interf

import app.dao.hibernate.FieldDaoImpl
import app.dao.hibernate.ParadigmDaoImpl
import app.model.Algorithm
import app.model.DesignParadigm
import app.model.FieldOfStudy
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class AlgorithmDaoImplTest extends Specification {
  @Shared
  DesignParadigm paradigm
  @Shared
  DesignParadigm updatedParadigm
  @Shared
  FieldOfStudy field
  @Shared
  FieldOfStudy updatedField
  @Shared
  Algorithm algorithm
  @Shared
  def paradigmDao = new ParadigmDaoImpl()
  @Shared
  def fieldDao = new FieldDaoImpl()
  @Shared
  def jdbcDao = new app.dao.jdbc.AlgorithmDaoImpl()
  @Shared
  def hibernateDao = new app.dao.hibernate.AlgorithmDaoImpl()

  def setup() {
    paradigm = new DesignParadigm("Test design paradigm")
    updatedParadigm = new DesignParadigm("Test updated design paradigm")
    field = new FieldOfStudy("Test field of study")
    updatedField = new FieldOfStudy("Test updated field of study")
    algorithm = new Algorithm("test name", "test complexity", paradigm, field)
    paradigmDao.persist(paradigm)
    paradigmDao.persist(updatedParadigm)
    fieldDao.persist(field)
    fieldDao.persist(updatedField)
  }

  def cleanup() {
    paradigmDao.delete(paradigm)
    paradigmDao.delete(updatedParadigm)
    fieldDao.delete(field)
    fieldDao.delete(updatedField)
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
    algorithmDao.delete(algorithm)
    where:
    algorithmDao << [hibernateDao, jdbcDao]
  }

  @Unroll
  def "test algorithm deletion"() {
    setup:
    algorithmDao.persist(algorithm)
    algorithmDao.delete(algorithm)
    expect:
    !algorithmDao.containsAlgorithm(algorithm.getName())
    where:
    algorithmDao << [hibernateDao, jdbcDao]
  }

  def "test set name"() {
    setup:
    algorithmDao.persist(algorithm)
    algorithmDao.setName(algorithm, "Test updated name")
    expect:
    !algorithmDao.containsAlgorithm(algorithm.getName())
    algorithmDao.containsAlgorithm("Test updated name")
    cleanup:
    algorithmDao.delete(algorithm)
    paradigmDao.delete(paradigm)
    paradigmDao.delete(updatedParadigm)
    fieldDao.delete(field)
    fieldDao.delete(updatedField)
    where:
    algorithmDao << [hibernateDao, jdbcDao]
  }
}
