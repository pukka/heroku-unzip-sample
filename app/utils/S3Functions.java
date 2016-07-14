package utils;

import java.io.File;

import javax.inject.Inject;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import services.S3Interface;

/**
 * This controller provide a s3 actions
 */
public class S3Functions {

    private static S3Interface s3;

    /** Defoult images path of s3 */
    private static final String DEFAULT_IMG_PATH = "image";

    @Inject
    public S3Functions(S3Interface s3) {
        this.s3 = s3;
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
     * @param uplaod object(File), id(String)
     * @return key of s3 if success upload
     */
    public static String uploadObj(File obj, String id) {
        String key = getImgKey(id);
        PutObjectRequest putObjectRequest = 
            new PutObjectRequest(s3.getBucket(), key, obj);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        s3.getCtl().putObject(putObjectRequest);

        return key;
    }
}
