package app.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractDao {
  private static EntityManagerFactory sessionFactory;
  private static EntityManager entityManager;

  static {
    sessionFactory = Persistence.createEntityManagerFactory("com.algorithm.manager");
    entityManager = sessionFactory.createEntityManager();
  }

  protected EntityManager getEntityManager() {
    return entityManager;
  }

  public void cleanUp() {
    entityManager.close();
    sessionFactory.close();
  }
}
