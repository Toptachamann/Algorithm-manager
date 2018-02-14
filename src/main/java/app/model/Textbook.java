package app.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Textbook {
  private Integer id;
  private String title;
  private Integer volume;
  private Integer edition;
  private List<Author> authors;

  public Textbook() {}

  public Textbook(
      Integer id, String title, @Nullable Integer volume, Integer edition, List<Author> authors) {

    this.id = id;
    this.title = title;
    this.volume = volume;
    this.edition = edition;
    this.authors = authors;
  }

  public Textbook(Integer id, String title, @Nullable Integer volume, Integer edition) {
    this.id = id;
    this.title = title;
    this.volume = volume;
    this.edition = edition;
    authors = new ArrayList<>();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Textbook textbook = (Textbook) o;

    return new EqualsBuilder().append(id, textbook.id).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).toHashCode();
  }

  public void addAuthor(Author author) {
    this.authors.add(author);
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
