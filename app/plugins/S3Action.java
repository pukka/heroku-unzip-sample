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

     @Inject
     public S3Action(ApplicationLifecycle lifecycle) {
         Logger.info("Readed Module");
         lifecycle.addStopHook(() -> {
             return null;
         });
     }
}
