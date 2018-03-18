package in.frisc.distroid;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by irshad on 08/03/18.
 */

public class Utils {
    private static final String TAG = "UTILS DISTROID";

    public static final int DISTROID_PORT = 52287;
    public static String serverIP = "NONE";

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
}
