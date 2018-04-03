package app.dao.interf

import app.model.AreaOfUse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class AreaDaoImplTest extends Specification {
  @Shared
  def jdbcDao = new app.dao.jdbc.AreaDaoImpl()
  @Shared
  def hibernateDao = new app.dao.hibernate.AreaDaoImpl()

  @Shared
  AreaOfUse area

  def setup(){
    area = new AreaOfUse("Test area of use")
  }

  @Unroll
  def "test area creation"(){
    when:
    areaDao.persist(area)
    then:
    areaDao.containsAreaOfUse(area.getAreaOfUse())
    def optCreated = areaDao.getAreaById(area.getId())
    optCreated.isPresent()
    optCreated.get() == area
    cleanup:
    areaDao.delete(area)
    where:
    areaDao << [jdbcDao, hibernateDao]
  }

  @Unroll
  def "test area deletion"(){
    setup:
    areaDao.persist(area)
    when:
    areaDao.delete(area)
    then:
    !areaDao.containsAreaOfUse(area.getAreaOfUse())
    where:
    areaDao << [jdbcDao, hibernateDao]
  }

}
