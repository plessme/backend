package com.bongladesch.plessme.documents.usecase;

public interface IStorage {
  boolean createUserStorage(String id);

  boolean deleteUserStorage(String id);
}
