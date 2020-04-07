package com.bongladesch.plessme.documents.adapter.minio;

import javax.enterprise.context.ApplicationScoped;

import com.bongladesch.plessme.documents.usecase.IStorage;

@ApplicationScoped
public class MinioS3Storage implements IStorage {

    public boolean createUserStorage(String id) {
        return true;
    }

    public boolean deleteUserStorage(String id) {
        return true;
    }
}