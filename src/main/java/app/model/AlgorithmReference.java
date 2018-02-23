package app.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "algorithm_reference")
public class AlgorithmReference {
  @Id
  @Column(name = "reference_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int referenceId;

  @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
  @JoinColumn(name = "ref_algorithm_id", referencedColumnName = "algorithm_id",
      nullable = false, foreignKey = @ForeignKey(name = "fk_algorithm_reference"))
  private Algorithm algorithm;

  @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
  @JoinColumn(name = "ref_book_id", referencedColumnName = "book_id",
      nullable = false, foreignKey = @ForeignKey(name = "fk_book_reference"))
  private Book bookId;

  public AlgorithmReference() {

  }

  public AlgorithmReference(Algorithm algorithm, Book bookId) {
    this.algorithm = algorithm;
    this.bookId = bookId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("referenceId", referenceId)
        .append("algorithm", algorithm)
        .append("bookId", bookId)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    AlgorithmReference that = (AlgorithmReference) o;

    return new EqualsBuilder()
        .append(referenceId, that.referenceId)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(referenceId)
        .toHashCode();
  }

  public int getReferenceId() {

    return referenceId;
  }

  public void setReferenceId(int referenceId) {
    this.referenceId = referenceId;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  public Book getBookId() {
    return bookId;
  }

  public void setBookId(Book bookId) {
    this.bookId = bookId;
  }
}