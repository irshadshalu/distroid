package in.frisc.distroid.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by irshad on 02/04/18.
 *
 * File splitting and combining module
 *
 */



public class FileSplitCombine {

    public static List<String> splitFile(File f, int sizeOfFiles, String outputFolder) throws IOException {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...
        //you can change it to 0 if you want 000, 001, ...

        List<String> results = new ArrayList<>();

        byte[] buffer = new byte[sizeOfFiles];

        String fileName = f.getName();

        //try-with-resources to ensure closing stream
        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                String filePartName = String.format(Locale.ENGLISH, "%s.%03d", fileName, partCounter++);
                results.add(f.getParent() + File.separator + ".cache" + File.separator + filePartName);
                File newFile = new File(f.getParent() + File.separator + ".cache", filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
            return results;

        }
    }
    public static void combineFiles(List<File> files, String folderPath, String fileName) throws IOException {
        int partCounter = 1;


        long sizeOfOneFile = files.get(0).length();


        File newFile = new File(folderPath, fileName);
        try (FileOutputStream out = new FileOutputStream(newFile)) {

            int count = files.size();
            for (int i = 0; i < count; i++) {
                File cur = files.get(i);
                FileInputStream fis = new FileInputStream(cur);
                BufferedInputStream bis = new BufferedInputStream(fis);
                byte[] buffer = new byte[(int) cur.length()];
                bis.read(buffer);
                out.write(buffer, i, (int) cur.length());
            }
        }
    }
}
