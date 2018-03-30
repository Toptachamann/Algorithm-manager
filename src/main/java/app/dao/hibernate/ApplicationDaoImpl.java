package app.dao.hibernate;

import app.dao.interf.ApplicationDao;
import app.model.Algorithm;
import app.model.Application;
import app.model.AreaOfUse;
import app.model.Application_;


import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class ApplicationDaoImpl extends AbstractDao implements ApplicationDao {
  @Override
  public Application createApplication(Algorithm algorithm, AreaOfUse areaOfUse) {
    Application application = new Application(algorithm, areaOfUse);
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(application);
    entityManager.getTransaction().commit();
    entityManager.close();
    return application;
  }



  @Override
  public Optional<Application> getApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Application> query = builder.createQuery(Application.class);
    Root<Application> root = query.from(Application.class);
    query.where(
        builder.and(
            builder.equal(root.get(Application_.algorithm), algorithm),
            builder.equal(root.get(Application_.areaOfUse), areaOfUse)));
    List<Application> result = entityManager.createQuery(query).getResultList();
    entityManager.close();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<Application> delete = builder.createCriteriaDelete(Application.class);
    Root<Application> root = delete.from(Application.class);
    delete.where(
        builder.and(
            builder.equal(root.get(Application_.algorithm), algorithm),
            builder.equal(root.get(Application_.areaOfUse), areaOfUse)));
    entityManager.createQuery(delete).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public List<Application> getApplicationsByArea(AreaOfUse area) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Application> query = builder.createQuery(Application.class);
    Root<Application> root = query.from(Application.class);
    query.where(builder.equal(root.get(Application_.areaOfUse), area));
    List<Application> result = entityManager.createQuery(query).getResultList();
    entityManager.close();
    return result;
  }

  @Override
  public List<Application> getApplicationsByAlgorithm(Algorithm algorithm) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Application> query = builder.createQuery(Application.class);
    Root<Application> root = query.from(Application.class);
    query.where(builder.equal(root.get(Application_.algorithm), algorithm));
    List<Application> applications = entityManager.createQuery(query).getResultList();
    entityManager.close();
    return applications;
  }
}
