package com.pscheduler.serverless.manager;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Manager {

    private static volatile S3Manager instance;

    private static volatile AmazonS3 client;

    private S3Manager() {

        client = AmazonS3ClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .build();
    }

    public static void init() {

        if (instance == null) {
            synchronized(S3Manager.class) {
                if (instance == null)
                    instance = new S3Manager();
            }
        }
    }

    public static AmazonS3 client() {

        init();
        return S3Manager.client;
    }
}