package app.model;

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
