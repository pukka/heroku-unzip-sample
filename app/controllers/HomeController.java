package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import play.data.Form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;

import utils.ZipUtils;

import views.html.index;
import views.html.afterUpload;

import javax.inject.*;
import services.S3Interface;

/**
 * This controller contains an action to handle upload zip
 * and decompress zip
 */
public class HomeController extends Controller {

    private static final String ZIP_URL = "/tmp/";
    private final S3Interface s3;

    @Inject
    public HomeController(S3Interface s3) {
       this.s3 = s3;
    }

    /**
     * An action that renders a upload zip page 
     */
    public Result index() {
        Logger.info("render top page");
        s3.sayHello();
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
            ZipUtils.unZip(ZIP_URL + filename, ZIP_URL);
            File dir = new File(ZIP_URL);
            String[] dirList = dir.list();
            return ok(afterUpload.render(Arrays.asList(dirList)));
        } else {
            return badRequest("error");
        }
    }
}
