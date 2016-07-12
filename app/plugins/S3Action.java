package modules;

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

import com.google.inject.Provider;

@Singleton
class S3Action implements S3Interface {

    public static AmazonS3 amazonS3;

    private void init() {
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        String s3Bucket  = System.getenv("AWS_S3_BUCKET");

        if ((accessKey != null) && (secretKey != null)) {
            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            amazonS3 = new AmazonS3Client(awsCredentials);
            amazonS3.createBucket(s3Bucket);
            Logger.info("Using S3 Bucket: " + s3Bucket);
        }
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
