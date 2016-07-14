package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtils {
    /**encoding : UTF-8*/
    private static final String DEFAULT_CHARSET = "UTF-8";
    /**buffer size 1024*/
    private static final int CACHE_SIZE = 1024;

    /**
     * unZip
     * @param zipFilePath
     * @param destDir
     * @return unzip dir
     */
    public static String unZip(String zipFilePath, String destDir) {
        ZipFile zipFile = null;
        try {
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            zipFile = new ZipFile(zipFilePath, DEFAULT_CHARSET);
            Enumeration<ZipEntry> zipEntries = zipFile.getEntries();
            File file, parentFile;
            ZipEntry entry;
            byte[] cache = new byte[CACHE_SIZE];
            while (zipEntries.hasMoreElements()) {
                entry = (ZipEntry) zipEntries.nextElement();
                if (entry.isDirectory()) {
                    new File(destDir + entry.getName()).mkdirs();
                    continue;
                }
                bis = new BufferedInputStream(zipFile.getInputStream(entry));
                file = new File(destDir + entry.getName());
                parentFile = file.getParentFile();
                if (parentFile != null && (!parentFile.exists())) {
                    parentFile.mkdirs();
                }
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, CACHE_SIZE);
                int readIndex = 0;
                while ((readIndex = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                    fos.write(cache, 0, readIndex);
                }
                bos.flush();
                bos.close();
                fos.close();
                bis.close();
                return destDir + getPreffix(entry.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return zipFilePath;
        }finally{
            try {
            	if (zipFile != null) {
                    zipFile.close();
            	}
                 return zipFilePath;
            } catch (IOException e) {
                e.printStackTrace();
                return zipFilePath;
            }
        }
    }
    /**
     * Get a prefix
     * @param filename 
     * @return filename
     */
    public static String getPreffix(String fileName) {
        if (fileName == null)
            return null;
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(0, point);
        } 
        return fileName;
    }
}
