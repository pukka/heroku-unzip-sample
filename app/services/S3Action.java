package services;

import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;

import play.Application;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.google.inject.Provider;

@Singleton
public class S3Action implements S3Interface {

    private static AmazonS3 ctl;
    private static String s3Bucket;
    private static String endpoint;
    /** Defoult images path of s3 */
    private static final String DEFAULT_IMG_PATH = "image";

    private void init() {
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        s3Bucket  = System.getenv("AWS_S3_BUCKET");
        endpoint = System.getenv("AWS_S3_ENDPOINT");
  
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

    /**
     * Returns the key(path) of s3 bucket
     * @param id(String)
     * @return key of s3 bucket
     */
    public static String getImgKey(String id) {
        return DEFAULT_IMG_PATH + "/" + id;
    }

    /**
     * An action that upload obj to s3
     * @param uplaod interfase s3(S3Interface), image object(File), id(String)
     * @return key of s3 if success upload
     */
    public static String putImg(S3Interface s3, File img, String id) {
         String key = getImgKey(id);
         PutObjectRequest putObjectRequest =
           new PutObjectRequest(s3Bucket, key, img);
         putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
         try {
             ctl.putObject(putObjectRequest);
         }catch(AmazonClientException ex){
             Logger.debug("RuntimeException");
         }finally{
             Logger.debug("Can not upload s3.");
         }
         Logger.info("push to :" + s3Bucket + "/" + key);

         return endpoint + s3Bucket + "/" + key;
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
