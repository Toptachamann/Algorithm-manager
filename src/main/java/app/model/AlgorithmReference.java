package app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "algorithm_reference")
public class AlgorithmReference {

  @Id
  @Column(name = "ref_algorithm_id")
  private int algorithmId;

  @Id
  @Column(name = "ref_book_id")
  private int bookId;

  public AlgorithmReference() {

  }

  public AlgorithmReference(int algorithmId, int bookId) {

    this.algorithmId = algorithmId;
    this.bookId = bookId;
  }

  public int getAlgorithmId() {
    return algorithmId;
  }

  public void setAlgorithmId(int algorithmId) {
    this.algorithmId = algorithmId;
  }

  public int getBookId() {
    return bookId;
  }

  public void setBookId(int bookId) {
    this.bookId = bookId;
  }
}
