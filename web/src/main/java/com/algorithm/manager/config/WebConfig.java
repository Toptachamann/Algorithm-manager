package com.algorithm.manager.config;

import com.algorithm.manager.dao.hibernate.AlgorithmDaoImpl;
import com.algorithm.manager.dao.hibernate.ApplicationDaoImpl;
import com.algorithm.manager.dao.hibernate.AreaDaoImpl;
import com.algorithm.manager.dao.hibernate.FieldDaoImpl;
import com.algorithm.manager.dao.hibernate.ParadigmDaoImpl;
import com.algorithm.manager.dao.interf.AlgorithmDao;
import com.algorithm.manager.dao.interf.ApplicationDao;
import com.algorithm.manager.dao.interf.AreaDao;
import com.algorithm.manager.dao.interf.FieldDao;
import com.algorithm.manager.dao.interf.ParadigmDao;
import com.algorithm.manager.service.AlgorithmService;
import com.algorithm.manager.service.AlgorithmServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("com.algorithm.manager.rest")
public class WebConfig implements WebMvcConfigurer {
  @Bean
  public AlgorithmService algorithmService() {
    return new AlgorithmServiceImpl(
        algorithmDao(), paradigmDao(), fieldDao(), areaDao(), applicationDao());
  }

  @Bean
  public AlgorithmDao algorithmDao() {
    return new AlgorithmDaoImpl();
  }

  @Bean
  public ParadigmDao paradigmDao() {
    return new ParadigmDaoImpl();
  }

  @Bean
  public FieldDao fieldDao() {
    return new FieldDaoImpl();
  }

  @Bean
  public AreaDao areaDao() {
    return new AreaDaoImpl();
  }

  @Bean
  public ApplicationDao applicationDao() {
    return new ApplicationDaoImpl();
  }
}
