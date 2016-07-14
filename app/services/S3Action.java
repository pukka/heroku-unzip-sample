package services;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.Application;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;

import com.google.inject.Provider;

@Singleton
public class S3Action implements S3Interface {

    private static AmazonS3 ctl;
    private static String s3Bucket;

    private void init() {
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        s3Bucket  = System.getenv("AWS_S3_BUCKET");

        if ((accessKey != null) && (secretKey != null)) {
            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            ctl = new AmazonS3Client(awsCredentials);
            try {
                ctl.getBucketAcl(s3Bucket);
                Logger.info("Using S3 Bucket: " + s3Bucket);
            } catch (AmazonS3Exception ex){
                Logger.info(ex.toString());
            }
        }
    }
    
    @Override
    public AmazonS3 getCtl() {
        return ctl;
    }
 
    @Override
    public String getBucket() {
        return s3Bucket;
    }
 
    @Inject
    public S3Action(ApplicationLifecycle lifecycle) {

        init();

        Logger.info("Readed Module");
        lifecycle.addStopHook(() -> {
             return null;
        });
    }
}
