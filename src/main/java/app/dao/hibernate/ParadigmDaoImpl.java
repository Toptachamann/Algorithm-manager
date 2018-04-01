package app.dao.hibernate;

import app.dao.interf.ParadigmDao;
import app.model.DesignParadigm;
import app.model.DesignParadigm_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class ParadigmDaoImpl extends AbstractDao implements ParadigmDao {

  public ParadigmDaoImpl() {}

  @Override
  public void create(DesignParadigm paradigm) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(paradigm);
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public List<DesignParadigm> getAll() {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    List<DesignParadigm> paradigms =
        entityManager
            .createQuery(criteria.select(criteria.from(DesignParadigm.class)))
            .getResultList();
    entityManager.close();
    return paradigms;
  }

  @Override
  public Optional<DesignParadigm> findById(int id) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    Root<DesignParadigm> root = criteria.from(DesignParadigm.class);
    List<DesignParadigm> paradigms =
        entityManager
            .createQuery(
                criteria.select(root).where(builder.equal(root.get(DesignParadigm_.id), id)))
            .getResultList();
    entityManager.close();
    return paradigms.size() == 1 ? Optional.of(paradigms.get(0)) : Optional.empty();
  }

  @Override
  public Optional<DesignParadigm> findByParadigm(String paradigm) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<DesignParadigm> criteria = builder.createQuery(DesignParadigm.class);
    Root<DesignParadigm> root = criteria.from(DesignParadigm.class);
    List<DesignParadigm> paradigms =
        entityManager
            .createQuery(
                criteria
                    .select(root)
                    .where(builder.equal(root.get(DesignParadigm_.paradigm), paradigm)))
            .getResultList();
    entityManager.close();
    return paradigms.size() == 1 ? Optional.of(paradigms.get(0)) : Optional.empty();
  }

  @Override
  public void deleteDesignParadigmById(int id) throws Exception {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<DesignParadigm> delete = builder.createCriteriaDelete(DesignParadigm.class);
    Root<DesignParadigm> root = delete.from(DesignParadigm.class);
    entityManager
        .createQuery(delete.where(builder.equal(root.get(DesignParadigm_.id), id)))
        .executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public void updateDesignParadigm(String newName, int id) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<DesignParadigm> update = builder.createCriteriaUpdate(DesignParadigm.class);
    Root<DesignParadigm> root = update.from(DesignParadigm.class);
    entityManager
        .createQuery(
            update
                .set(DesignParadigm_.paradigm, newName)
                .where(builder.equal(root.get(DesignParadigm_.id), id)))
        .executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
