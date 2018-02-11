package app.model;

import java.util.ArrayList;
import java.util.List;

public class Textbook {
  private int id;
  private String title;
  private int volume;
  private int edition;
  private List<Author> authors;

  public Textbook() {}

  public Textbook(int id, String title, int volume, int edition, List<Author> authors) {

    this.id = id;
    this.title = title;
    this.volume = volume;
    this.edition = edition;
    this.authors = authors;
  }

  public Textbook(int id, String title, int volume, int edition) {
    this.id = id;
    this.title = title;
    this.volume = volume;
    this.edition = edition;
    authors = new ArrayList<>();
  }

  public void addAuthor(Author author) {
    this.authors.add(author);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getVolume() {
    return volume;
  }

  public void setVolume(int volume) {
    this.volume = volume;
  }

  public int getEdition() {
    return edition;
  }

  public void setEdition(int edition) {
    this.edition = edition;
  }

  public List<Author> getAuthors() {
    return authors;
  }

  public void setAuthors(List<Author> authors) {
    this.authors = authors;
  }
}
