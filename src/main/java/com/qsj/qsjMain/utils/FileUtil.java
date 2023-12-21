package com.qsj.qsjMain.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class FileUtil {
    public static String saveAvatar(Long userId, String base64Content) {
        // write to local file
        String fileName = "avatar_" + userId.toString() + "_" + System.currentTimeMillis() + ".jpg";
        // check local system type
        String osName = System.getProperty("os.name");
        String filePath;
        if (osName.toLowerCase().startsWith("win")) {
            filePath = "resources/" + fileName;
        } else {
            filePath = "/data/qsj/web/static/avatar/" + fileName;
        }
        // convert base64 to byte[]
        if(base64Content.startsWith("data:image/jpeg;base64,")){
            base64Content = base64Content.replace("data:image/jpeg;base64,", "");
        }
        byte[] bytes = Base64.getDecoder().decode(base64Content);
        // write bytes to file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "https://static.lightmeal-service.com/static/avatar/" + fileName;
    }

    public static String saveQRCode(String orderId, Long orderItemId, String base64Content) {
        String fileName = "qrcode_" + orderId + "_" + orderItemId + "_" + System.currentTimeMillis() + ".jpg";
        String filePath;
        // check local system type
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("win")) {
            filePath = "resources/" + fileName;
        } else {
            filePath = "/data/qsj/web/static/qrcode/" + fileName;
        }

        byte[] bytes = Base64.getDecoder().decode(base64Content);
        // write bytes to file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "https://static.lightmeal-service.com/static/qrcode/" + fileName;
    }
}
