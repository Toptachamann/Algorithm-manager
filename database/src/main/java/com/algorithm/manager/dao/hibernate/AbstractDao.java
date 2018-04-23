package com.algorithm.manager.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractDao {
  private static SessionFactory sessionFactory;
  private static ThreadLocal<Session> threadLocal;

  static {
    sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("com.algorithm.manager");
    threadLocal = new ThreadLocal<>();
  }

    public AbstractDao() {
    }

    protected static Session getSession() {
    Session entityManager = threadLocal.get();
    if(entityManager == null){
      entityManager = sessionFactory.openSession();
      threadLocal.set(entityManager);
    }
    return entityManager;
  }

  public void cleanUp() {
    threadLocal.get().close();
    sessionFactory.close();
  }
}
