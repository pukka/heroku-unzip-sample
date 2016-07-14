package services;

import com.amazonaws.services.s3.AmazonS3;

public interface S3Interface {
    public AmazonS3 getCtl();
    public String getBucket();
}
