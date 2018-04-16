package com.algorithm.manager.dao.interf

import com.algorithm.manager.model.AreaOfUse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class AreaDaoImplTest extends Specification {
    @Shared
    def jdbcDao = new com.algorithm.manager.dao.jdbc.AreaDaoImpl()
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
        areaDao.containsAreaOfUse(area.getAreaOfUse())
        def optCreated = areaDao.getAreaById(area.getId())
        optCreated.isPresent()
        optCreated.get() == area
        cleanup:
        areaDao.deleteById(area)
        where:
        areaDao << [jdbcDao, hibernateDao]
    }

    @Unroll
    def "test area deletion"() {
        setup:
        areaDao.persist(area)
        when:
        areaDao.deleteById(area)
        then:
        !areaDao.containsAreaOfUse(area.getAreaOfUse())
        where:
        areaDao << [jdbcDao, hibernateDao]
    }

}
