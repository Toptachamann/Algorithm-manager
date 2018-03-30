package app.dao.hibernate;

import app.dao.interf.AreaDao;
import app.model.AreaOfUse;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class AreaDaoImpl extends AbstractDao implements AreaDao {
  @Override
  public List<AreaOfUse> getAllAreas() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    query.from(AreaOfUse.class);
    List<AreaOfUse> areas = entityManager.createQuery(query).getResultList();
    entityManager.close();
    return areas;
  }

  @Override
  public AreaOfUse createAreaOfUse(String area, String description) {
    AreaOfUse areaOfUse = new AreaOfUse(area, description);
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(areaOfUse);
    entityManager.getTransaction().commit();
    entityManager.close();
    return areaOfUse;
  }

  @Override
  public AreaOfUse createAreaOfUse(String area) {
    return createAreaOfUse(area, null);
  }

  @Override
  public Optional<AreaOfUse> getAreaById(int id) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    Root<AreaOfUse> root = query.from(AreaOfUse.class);
    List<AreaOfUse> result =
        entityManager
            .createQuery(query.where(builder.equal(root.get(AreaOfUse_.id), id)))
            .getResultList();
    entityManager.close();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public Optional<AreaOfUse> getAreaByName(String name) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AreaOfUse> query = builder.createQuery(AreaOfUse.class);
    Root<AreaOfUse> root = query.from(AreaOfUse.class);
    List<AreaOfUse> result =
        entityManager
            .createQuery(builder.equal(root.get(AreaOfUse_.areaOfUse), name))
            .getResultList();
    entityManager.close();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  @Override
  public void deleteAreaOfUse(int id) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<AreaOfUse> delete = builder.createCriteriaDelete(AreaOfUse.class);
    Root<AreaOfUse> root = delete.from(AreaOfUse.class);
    entityManager
        .createQuery(delete.where(builder.equal(root.get(AreaOfUse_.id), id)))
        .executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
