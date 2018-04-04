package app.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractDao {
  private static EntityManagerFactory sessionFactory;
  private static ThreadLocal<EntityManager> threadLocal;

  static {
    sessionFactory = Persistence.createEntityManagerFactory("com.algorithm.manager");
    threadLocal = new ThreadLocal<>();
  }

   protected static EntityManager getEntityManager() {
    EntityManager entityManager = threadLocal.get();
    if(entityManager == null){
      entityManager = sessionFactory.createEntityManager();
      threadLocal.set(entityManager);
    }
    return entityManager;
  }

  public void cleanUp() {
    threadLocal.get().close();
    sessionFactory.close();
  }
}
