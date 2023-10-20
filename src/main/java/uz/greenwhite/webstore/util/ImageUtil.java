package uz.greenwhite.webstore.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import uz.greenwhite.webstore.entity.Product;

import java.io.*;
import java.util.Objects;

@Component
public class ImageUtil {
    private static final String FILE_ROOT = "src/main/resources/static/FILES";

    public static String saveImage(String folderName, String fileName, MultipartFile file) throws IOException {
        String directory = FILE_ROOT + "/" + folderName;

        if(new File(directory).mkdir()) return "";

        String ext = "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        File sf = new File(directory + "/"+ fileName + ext);
        BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(sf));

        bw.write(file.getBytes());
        bw.close();

        return sf.getName();
    }

    public static void getImage(String folderName, String fileName, HttpServletResponse response) {
        File file = new File(FILE_ROOT + "/" + folderName +"/" + fileName);
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("inline; filename=\"%s\"", file.getName());
            response.setHeader(headerKey, headerValue);
            FileInputStream inputStream;
            try {
                inputStream = new FileInputStream(file);
                try {
                    int c;
                    while ((c = inputStream.read()) != -1) {
                        response.getWriter().write(c);
                    }
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.getWriter().close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
