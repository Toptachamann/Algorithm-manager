package app.dao.hibernate;

import app.dao.interf.AuthorDao;
import app.model.Author;
import app.model.Author_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl extends AbstractDao implements AuthorDao {
  @Override
  public Author createAuthor(String firstName, String lastName) {
    Author author = new Author(firstName, lastName);
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(author);
    entityManager.getTransaction().commit();
    entityManager.close();
    return author;
  }

  @Override
  public void deleteAuthor(Author author) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaDelete<Author> delete = builder.createCriteriaDelete(Author.class);
    Root<Author> root = delete.from(Author.class);
    delete.where(builder.equal(root.get(Author_.id), author.getId()));
    entityManager.createQuery(delete).executeUpdate();
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Override
  public Optional<Author> getAuthor(String firstName, String lastName) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Author> query = builder.createQuery(Author.class);
    Root<Author> root = query.from(Author.class);
    query.where(
        builder.and(
            builder.equal(root.get(Author_.firstName), firstName),
            builder.equal(root.get(Author_.lastName), lastName)));
    List<Author> result = entityManager.createQuery(query).getResultList();
    entityManager.close();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }
}
