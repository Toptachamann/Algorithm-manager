package com.algorithm.manager.dao.interf

import com.algorithm.manager.model.AreaOfUse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class AreaDaoImplTest extends Specification {
  @Shared
  def hibernateDao = new com.algorithm.manager.dao.hibernate.AreaDaoImpl()

  @Shared
  AreaOfUse area

  def setup() {
    area = new AreaOfUse("Test area of use")
  }

  @Unroll
  def "test area creation"() {
    when:
    areaDao.persist(area)
    then:
    areaDao.containsAreaOfUseWithName(area.getName())
    def optCreated = areaDao.getAreaOfUseById(area.getId())
    optCreated.isPresent()
    optCreated.get() == area
    cleanup:
    areaDao.delete(area)
    where:
    areaDao << [hibernateDao]
  }

  @Unroll
  def "test area deletion"() {
    setup:
    areaDao.persist(area)
    when:
    areaDao.delete(area)
    then:
    !areaDao.containsAreaOfUseWithName(area.getName())
    where:
    areaDao << [hibernateDao]
  }

}
