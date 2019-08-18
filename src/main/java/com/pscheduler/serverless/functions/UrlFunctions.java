package com.pscheduler.serverless.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pscheduler.serverless.dao.S3UrlDao;

public class UrlFunctions implements RequestHandler<UrlRequest, String> {

    private static final S3UrlDao s3Dao = S3UrlDao.instance();

    @Override
    public String handleRequest(UrlRequest request, Context context) {
        return s3Dao.storeData(request.data, request.prefix);
    }

}
