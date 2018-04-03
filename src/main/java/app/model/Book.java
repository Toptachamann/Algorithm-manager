package app.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
  @Id
  @Column(name = "book_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "title", nullable = false, length = 50)
  private String title;

  @Column(name = "volume")
  private Integer volume;

  @Column(name = "edition")
  private int edition;
  @ManyToMany(
    cascade = {CascadeType.REFRESH},
    targetEntity = Author.class
  )
  @JoinTable(
    name = "textbook",
    joinColumns = @JoinColumn(name = "txtbk_book_id"),
    inverseJoinColumns = @JoinColumn(name = "txtbk_author_id")
  )
  private List<Author> authors;

  public Book(String title, Integer volume, int edition, List<Author> authors) {
    this.title = title;
    this.volume = volume;
    this.edition = edition;
    this.authors = authors;
  }

  public Book() {}

  public Book(
      Integer id, String title, @Nullable Integer volume, Integer edition, List<Author> authors) {
    this.id = id;
    this.title = title;
    this.volume = volume;
    this.edition = edition;
    this.authors = authors;
  }

  public Book(Integer id, String title, @Nullable Integer volume, Integer edition) {
    this.id = id;
    this.title = title;
    this.volume = volume;
    this.edition = edition;
    authors = new ArrayList<>();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("title", title)
        .append("volume", volume)
        .append("edition", edition)
        .append("authors", authors)
        .toString();
  }

  public void addAuthor(Author author) {
    this.authors.add(author);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Book book = (Book) o;

    return new EqualsBuilder().append(id, book.id).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).toHashCode();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getVolume() {
    return volume;
  }

  public void setVolume(Integer volume) {
    this.volume = volume;
  }

  public Integer getEdition() {
    return edition;
  }

  public void setEdition(Integer edition) {
    this.edition = edition;
  }

  public List<Author> getAuthors() {
    return authors;
  }

  public void setAuthors(List<Author> authors) {
    this.authors = authors;
  }
}
