package com.bongladesch.plessme.documents.entity;

import java.util.Collection;
import java.util.Date;

public class Document {

  private String id;
  private Date creation;
  private Date update;
  private String content;
  private Collection<String> labels;
  private Collection<String> categories;

  private Document(
      final String id,
      final Date creation,
      final Date update,
      final String content,
      final Collection<String> labels,
      final Collection<String> categories) {
    this.id = id;
    this.creation = creation;
    this.update = update;
    this.content = content;
    this.labels = labels;
    this.categories = categories;
  }

  public static class DocumentBuilder {
    private String id;
    private Date creation;
    private Date update;
    private String content;
    private Collection<String> labels;
    private Collection<String> categories;

    public DocumentBuilder() {}

    public DocumentBuilder id(String id) {
      this.id = id;
      return this;
    }

    public DocumentBuilder creation(Date creation) {
      this.creation = creation;
      return this;
    }

    public DocumentBuilder content(String content) {
      this.content = content;
      return this;
    }

    public DocumentBuilder update(Date update) {
      this.update = update;
      return this;
    }

    public DocumentBuilder label(Collection<String> labels) {
      this.labels = labels;
      return this;
    }

    public DocumentBuilder categories(Collection<String> categories) {
      this.categories = categories;
      return this;
    }

    public Document build() {
      return new Document(
          this.id, this.creation, this.update, this.content, this.labels, this.categories);
    }
  }

  public String getId() {
    return this.id;
  }

  public Date getCreation() {
    return this.creation;
  }

  public Date getUpdate() {
    return this.update;
  }

  public String getContent() {
    return this.content;
  }

  public Collection<String> getLabels() {
    return this.labels;
  }

  public Collection<String> getCategories() {
    return this.categories;
  }
}
