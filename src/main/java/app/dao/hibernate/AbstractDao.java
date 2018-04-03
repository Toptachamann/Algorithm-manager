package app.dao.hibernate;

import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractDao {
  private static EntityManagerFactory sessionFactory =
      Persistence.createEntityManagerFactory("com.algorithm.manager");

  protected EntityManager getEntityManager() {
    return sessionFactory.createEntityManager();
  }

  public void cleanUp() {
    sessionFactory.close();
  }
}
