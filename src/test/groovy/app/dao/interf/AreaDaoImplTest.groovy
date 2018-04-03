package app.dao.interf

import app.model.AreaOfUse
import spock.lang.Shared
import spock.lang.Specification

class AreaDaoImplTest extends Specification {

  @Shared
  AreaOfUse area

  def setup(){
    area = new AreaOfUse("Test area of use")
  }

}
