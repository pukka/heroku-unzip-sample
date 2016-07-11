package controllers;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import play.data.Form;

import java.io.File;
import java.io.FileNotFoundException;
import utils.ZipUtils;

import views.html.index;

/**
 * This controller contains an action to handle upload zip
 * and decompress zip
 */
public class HomeController extends Controller {
    /**
     * An action that renders a upload zip page 
     */
    public Result index() {
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
            File nf = new File("/tmp/" + filename);
            boolean rst = file.renameTo(nf);
            ZipUtils.unZip("/tmp/" + filename, filename);
        } else {
            return badRequest("error");
        }
        return redirect(routes.HomeController.index());
    }
}
