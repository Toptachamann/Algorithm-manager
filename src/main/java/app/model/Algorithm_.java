package app.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Algorithm.class)
public abstract class Algorithm_ {

	public static volatile SingularAttribute<Algorithm, String> complexity;
	public static volatile SingularAttribute<Algorithm, DesignParadigm> designParadigm;
	public static volatile SingularAttribute<Algorithm, String> name;
	public static volatile SingularAttribute<Algorithm, Integer> id;
	public static volatile SingularAttribute<Algorithm, FieldOfStudy> fieldOfStudy;

}

