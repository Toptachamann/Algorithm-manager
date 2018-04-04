package app.dao.interf

import app.dao.hibernate.AlgorithmDaoImpl
import app.dao.hibernate.BookDaoImpl
import app.dao.hibernate.FieldDaoImpl
import app.dao.hibernate.ParadigmDaoImpl
import app.model.Algorithm
import app.model.DesignParadigm
import app.model.FieldOfStudy
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ReferenceDaoImplTest extends Specification {
  @Shared
  def algorithmDao = new AlgorithmDaoImpl()
  @Shared
  def bookDao = new BookDaoImpl()
  @Shared
  def paradigmDao = new ParadigmDaoImpl()
  @Shared
  def fieldDao = new FieldDaoImpl()
  @Shared
  def paradigm = new DesignParadigm("test design paradigm")
  @Shared
  def field = new FieldOfStudy("test field of study")
  @Shared
  def testAlgorithm

  def setup(){
    testAlgorithm = new Algorithm("test name", "test complexity", paradigm, field)
  }

  @Unroll
  def "test reference creation"(){

  }

  @Unroll
  def "test reference deletion"(){

  }

}
