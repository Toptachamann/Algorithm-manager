package com.algorithm.manager.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "algorithm_reference")
public class Reference {
  @Id
  @Column(name = "reference_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(
    cascade = {CascadeType.REFRESH},
    optional = false
  )
  @JoinColumn(
    name = "ref_algorithm_id",
    referencedColumnName = "algorithm_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "fk_algorithm_reference")
  )
  private Algorithm algorithm;

  @ManyToOne(
    cascade = {CascadeType.REFRESH},
    optional = false
  )
  @JoinColumn(
    name = "ref_book_id",
    referencedColumnName = "book_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "fk_book_reference")
  )
  private Book book;

  public Reference() {}

  public Reference(int id, Algorithm algorithm, Book book) {
    this.id = id;
    this.algorithm = algorithm;
    this.book = book;
  }

  public Reference(Algorithm algorithm, Book book) {
    this.algorithm = algorithm;
    this.book = book;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("referenceId", id)
        .append("algorithm", algorithm)
        .append("bookId", book)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Reference reference = (Reference) o;

    return new EqualsBuilder()
        .append(id, reference.id)
        .append(algorithm, reference.algorithm)
        .append(book, reference.book)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(algorithm)
        .append(book)
        .toHashCode();
  }

  public int getId() {

    return id;
  }

  public void setId(int referenceId) {
    this.id = referenceId;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }
}
