package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import play.data.Form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import java.io.File;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import utils.ZipUtils;

import views.html.index;
import views.html.afterUpload;
import views.html.afterPutImg;

import services.S3Interface;
import services.S3Action;

/**
 * This controller contains an action to handle upload zip
 * and decompress zip
 */
public class HomeController extends Controller {

    private static final String ZIP_URL = "/tmp/";
    private static S3Interface s3;

    @Inject
    public HomeController(S3Interface s3) {
          this.s3 = s3;
    }

    /**
     * An action that renders a upload zip page 
     */
    public Result index() {
        Logger.info("render top page");
        return ok(index.render());
    }

    /**
     * An action that decompress zip 
     */
    public Result unzip() {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        MultipartFormData.FilePart<File> fileInput = body.getFile("inputFile");
        String filename = fileInput.getFilename();
        if (fileInput != null) {
            File file = (File) fileInput.getFile();
            File nf = new File(ZIP_URL + filename);
            boolean rst = file.renameTo(nf);
            String destDir = ZipUtils.unZip(ZIP_URL + filename, ZIP_URL);
            File dir = new File(ZIP_URL);
            String[] dirList = dir.list();
            return ok(afterUpload.render(Arrays.asList(dirList)));
        } else {
            return badRequest("error");
        }
    }

    /**
     * An action that upload s3
     */
    public Result uploadImage() {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        MultipartFormData.FilePart<File> fileInput = body.getFile("inputFile");
        if (fileInput != null) {
            File file = (File) fileInput.getFile();
            String imageUrl = S3Action.putImg(s3, file, UUID.randomUUID().toString());
            return ok(afterPutImg.render(imageUrl));
        } else {
            return badRequest("error");
        }
    }
}
