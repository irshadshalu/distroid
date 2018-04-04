package in.frisc.distroid;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import co.jasonwyatt.squeaky.Database;
import in.frisc.distroid.dummy.DummyContent;

/**
 * Created by irshad on 08/03/18.
 */

public class Utils {

    public static List<String> connectedIps = new ArrayList<>();
    private static final String TAG = "UTILS DISTROID";
    public static Database myDb;

    public static final int DISTROID_PORT = 52287;
    public static String serverIP = "NONE";
    public static boolean isServer = false;

    public static void getNetworkIPs() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    Log.w(TAG, "STARTING SCAN");
                    final byte[] ip;
                    ip = new byte[]{(byte) 192, (byte) 168, (byte)43, (byte)1};

                    for(int i=1;i<=254;i++) {
                        final int j = i;  // i as non-final variable cannot be referenced from inner class
                        new Thread(new Runnable() {   // new thread for parallel execution
                            public void run() {
                                try {
                                    ip[3] = (byte)j;
                                    InetAddress address = InetAddress.getByAddress(ip);
                                    String output = address.toString().substring(1);
//                                    if (address.isReachable(500)) {
                                    if(isAddressReachable(output, DISTROID_PORT, 4000)){
                                        Log.w(TAG, output + " is on the network");
                                        serverIP = output;
                                    } else {
                                        Log.w(TAG, "Not Reachable: "+output);
                                    }
                                } catch (Exception e) {
                                    Log.w(TAG,"ERROR : " + Integer.toString(j));
                                    e.printStackTrace();
                                }
                            }
                        }).start();     // dont forget to start the thread
                    }
                } catch (Exception e) {
                    Log.w(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private static boolean isAddressReachable(String address, int port, int timeout) {
        try {

            try (Socket crunchifySocket = new Socket()) {
                // Connects this socket to the server with a specified timeout value.
                crunchifySocket.connect(new InetSocketAddress(address, port), timeout);
            }
            // Return true if connection successful
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();

            // Return false if connection fails
            return false;
        }
    }

    public static void startServer() {

    }

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
                results.add(filePartName);
                File newFile = new File(f.getParent() + File.separator + ".cache", filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
            return results;

        }
    }


    public static boolean readFiles(File directory){

        try {
            final File[] files = directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    String pathname = file.getName().toLowerCase();
                    boolean isVideoOrImage = pathname.endsWith(".jpg")
                            || pathname.endsWith(".mp4")
                            || pathname.endsWith(".png")
                            || pathname.endsWith(".gif")
                            || pathname.endsWith(".jpeg")
                            || pathname.endsWith(".m4a")
                            || pathname.endsWith(".3gp")
                            || pathname.endsWith(".avi")
                            || pathname.endsWith(".mp4")
                            || pathname.endsWith(".mkv");
                    return isVideoOrImage && file.isFile();
                }
            });

            Arrays.sort( files, new Comparator()
            {
                public int compare(Object o1, Object o2) {

                    if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                        return -1;
                    } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });
//                new Thread(new Runnable() {
//                    public void run() {
            DummyContent.makeStatuses(files);
//                    }
//                }).start();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



}
