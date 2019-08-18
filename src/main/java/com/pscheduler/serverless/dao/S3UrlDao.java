package com.pscheduler.serverless.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pscheduler.serverless.manager.S3Manager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class S3UrlDao implements UrlDao {

    private static final AmazonS3 s3 = S3Manager.client();

    private static volatile S3UrlDao instance;

    private static final String BUCKET = System.getenv("bucket");

    private S3UrlDao() {}

    public static S3UrlDao instance() {
        if (instance == null) {
            synchronized(S3UrlDao.class) {
                if (instance == null) {
                    instance = new S3UrlDao();
                }
            }
        }
        return instance;
    }

    @Override
    public String storeData(String data, String prefix) {
        String key = generateShortId();
        int attempts = 0;
        while (attempts < 5) {
            try {
                s3.getObjectMetadata(BUCKET, prefix + "/" + key);
                attempts++;
                key = generateShortId();
            } catch (Exception ex) {
                InputStream stream  = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("application/json");
                metadata.setContentLength(data.length());
                PutObjectRequest request = new PutObjectRequest(BUCKET, prefix + "/" + key, stream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
                s3.putObject(request);
                return key;
            }
        }

        return null;
    }

    private String generateShortId() {
        String SALT_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        char[] salt = new char[10];
        Random rnd = new Random();
        for (int i = 0; i < salt.length; i++) {
            int index = (int) (rnd.nextFloat() * SALT_CHARS.length());
            salt[i] = SALT_CHARS.charAt(index);
        }
        return new String(salt);
    }

}
