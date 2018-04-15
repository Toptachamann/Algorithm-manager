package com.algorithm.manager.dao.hibernate;

import com.algorithm.manager.dao.interf.ReferenceDao;
import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Book;
import com.algorithm.manager.model.Reference;
import com.algorithm.manager.model.Reference_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class ReferenceDaoImpl extends AbstractDao implements ReferenceDao {
  public ReferenceDaoImpl() {}

  @Override
  public void persist(Reference reference) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(reference);
    entityManager.getTransaction().commit();
  }

  @Override
  public Optional<Reference> getReference(Algorithm algorithm, Book book) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Reference> query = builder.createQuery(Reference.class);
    Root<Reference> root = query.from(Reference.class);
    query.where(
        builder.and(
            builder.equal(root.get(Reference_.algorithm), algorithm),
            builder.equal(root.get(Reference_.book), book)));
    List<Reference> references = entityManager.createQuery(query).getResultList();
    return references.isEmpty() ? Optional.empty() : Optional.of(references.get(0));
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
    return result.size() == 1;
  }

  @Override
  public List<Reference> getAlgorithmReferences(Algorithm algorithm) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Reference> query = builder.createQuery(Reference.class);
    Root<Reference> root = query.from(Reference.class);
    query.where(builder.equal(root.get(Reference_.algorithm), algorithm));
    List<Reference> references = entityManager.createQuery(query).getResultList();
    return references;
  }

  @Override
  public void delete(Reference reference) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.remove(
        entityManager.contains(reference) ? reference : entityManager.merge(reference));
    entityManager.getTransaction().commit();
  }
}
