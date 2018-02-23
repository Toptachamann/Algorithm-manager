package app.dao.hibernate;

import app.dao.interf.FieldDao;
import app.model.FieldOfStudy;
import app.model.FieldOfStudy_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class FieldDaoImpl extends AbstractDao implements FieldDao {
  @Override
  public FieldOfStudy createFieldOfStudy(String field) {
    return createFieldOfStudy(field, null);
  }

  @Override
  public FieldOfStudy createFieldOfStudy(String field, String description) {
    FieldOfStudy fieldOfStudy = new FieldOfStudy(field, description);
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(fieldOfStudy);
    entityManager.getTransaction().commit();
    entityManager.close();
    return fieldOfStudy;
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteriaQuery = builder.createQuery(FieldOfStudy.class);
    entityManager.close();
    return entityManager
        .createQuery(criteriaQuery.select(criteriaQuery.from(FieldOfStudy.class)))
        .getResultList();
  }

  @Override
  public Optional<FieldOfStudy> getFieldByName(String name) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteria = builder.createQuery(FieldOfStudy.class);
    Root<FieldOfStudy> root = criteria.from(FieldOfStudy.class);
    List<FieldOfStudy> fields =
        entityManager
            .createQuery(
                criteria.select(root).where(builder.equal(root.get(FieldOfStudy_.field), name)))
            .getResultList();
    entityManager.close();
    return fields.size() == 1 ? Optional.of(fields.get(0)) : Optional.empty();
  }

  @Override
  public Optional<FieldOfStudy> getFieldById(int id) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FieldOfStudy> criteria = builder.createQuery(FieldOfStudy.class);
    Root<FieldOfStudy> root = criteria.from(FieldOfStudy.class);
    List<FieldOfStudy> fields =
        entityManager
            .createQuery(criteria.select(root).where(builder.equal(root.get(FieldOfStudy_.id), id)))
            .getResultList();
    entityManager.close();
    return fields.size() == 1 ? Optional.of(fields.get(0)) : Optional.empty();
  }

  @Override
  public void updateFieldOfStudy(String newName, int id) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<FieldOfStudy> criteria = builder.createCriteriaUpdate(FieldOfStudy.class);
    Root<FieldOfStudy> root = criteria.from(FieldOfStudy.class);
    entityManager
        .createQuery(
            criteria
                .set(FieldOfStudy_.field, newName)
                .where(builder.equal(root.get(FieldOfStudy_.id), id)))
        .executeUpdate();
    entityManager.getTransaction().commit();
  }
}
