package app.dao.hibernate;

import app.dao.interf.ReferenceDao;
import app.model.Algorithm;
import app.model.Book;
import app.model.Reference;
import app.model.Reference_;


import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ReferenceDaoImpl extends AbstractDao implements ReferenceDao {
  @Override
  public Reference createReference(Algorithm algorithm, Book book) {
    Reference reference = new Reference(algorithm, book);
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(reference);
    entityManager.getTransaction().commit();
    entityManager.close();
    return reference;
  }

  @Override
  public boolean containsReference(Algorithm algorithm, Book book) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Reference> query = builder.createQuery(Reference.class);
    Root<Reference> root = query.from(Reference.class);
    List<Reference> result =
        entityManager
            .createQuery(
                query.where(
                    builder.and(
                        builder.equal(root.get(Reference_.algorithm), algorithm),
                        builder.equal(root.get(Reference_.book), book))))
            .getResultList();
    entityManager.close();
    return result.size() == 1;
  }

  @Override
  public void deleteReference(Algorithm algorithm, Book book) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<Reference> delete = builder.createCriteriaDelete(Reference.class);
    Root<Reference> root = delete.from(Reference.class);
    entityManager
        .createQuery(
            delete.where(
                builder.and(
                    builder.equal(root.get(Reference_.algorithm), algorithm),
                    builder.equal(root.get(Reference_.book), book))))
        .executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
