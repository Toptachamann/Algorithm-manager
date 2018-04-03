package app.dao.hibernate;

import app.dao.interf.ApplicationDao;
import app.model.Algorithm;
import app.model.Application;
import app.model.Application_;
import app.model.AreaOfUse;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class ApplicationDaoImpl extends AbstractDao implements ApplicationDao {
  @Override
  public void persist(Application application) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(application);
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public Optional<Application> getApplicationOf(Algorithm algorithm, AreaOfUse areaOfUse)
      throws Exception {
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
  public void deleteApplication(Application application) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(entityManager.contains(application) ? application : entityManager.merge(application));
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
