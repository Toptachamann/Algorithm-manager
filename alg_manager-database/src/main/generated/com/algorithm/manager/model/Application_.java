package com.algorithm.manager.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Application.class)
public abstract class Application_ {

	public static volatile SingularAttribute<Application, Integer> id;
	public static volatile SingularAttribute<Application, AreaOfUse> areaOfUse;
	public static volatile SingularAttribute<Application, Algorithm> algorithm;

}

