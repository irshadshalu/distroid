package in.frisc.distroid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryption {
    public static void encryptAllChunks(String key, List<String> filePaths, String folderPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        int k = filePaths.size();
        File cacheDirectory = new File(folderPath + File.separator + ".encrypted");
        if (! cacheDirectory.exists()){
            cacheDirectory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        for (int i = 0; i < k; i++) {
            FileInputStream fis = new FileInputStream(filePaths.get(i));
            FileOutputStream fos = new FileOutputStream(folderPath + File.separator + ".encrypted/temp_" +  String.valueOf(i));

            SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);

            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            // Write bytes
            int b;
            byte[] d = new byte[8];
            while ((b = fis.read(d)) != -1) {
                cos.write(d, 0, b);
            }

            cos.flush();
            cos.close();
            fis.close();

            //delete the original file
            File file = new File(filePaths.get(i));
            file.delete();
        }
    }

    public void decryptAllChunks(String key, String[] filePaths, String folderPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        int k = filePaths.length;
        for (int i = 0; i < k; i++) {
            FileInputStream fis = new FileInputStream(folderPath + File.separator + ".encrypted " + String.valueOf(i));

            FileOutputStream fos = new FileOutputStream(filePaths[i]);
            SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int b;
            byte[] d = new byte[8];
            while ((b = cis.read(d)) != -1) {
                fos.write(d, 0, b);
            }
            fos.flush();
            fos.close();
            cis.close();

            //delete the encrypted file
            File file = new File(folderPath + "/encrypted " + String.valueOf(i));
            file.delete();
        }
    }
}