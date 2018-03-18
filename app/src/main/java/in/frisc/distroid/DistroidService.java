package in.frisc.distroid;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class DistroidService extends Service {
    private static final String TAG = "DISTROID SERVICE";
    FileObserver observer;
    SharedPreferences sharedPreferences;
    String folderPath;

    DistroidServer distroidServer;


    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Thread.NORM_PRIORITY);
        thread.start();

        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        final String folderName = sharedPreferences.getString(getString(R.string.key_backup_directory), "DistroidTest");
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
        folderPath = android.os.Environment.getExternalStorageDirectory().toString() + "/" + folderName;
        Log.e(TAG, folderPath);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Utils.serverIP.equals("NONE")) {

                    distroidServer = new DistroidServer(DistroidService.this, Utils.DISTROID_PORT);
                    (new Handler(Looper.getMainLooper())).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "New server created", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{

                    (new Handler(Looper.getMainLooper())).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Connected to existing temporary server: "+ Utils.serverIP, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        },10000);

        File directory = new File(folderPath);
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }

        observer = new FileObserver(folderPath ) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, String file) {

                if(event == FileObserver.CREATE && !file.equals(".probe")){ // check if its a "create" and not equal to .probe because thats created every time camera is launched
                    final String msg = "Distroid: New file created [" + folderPath + "/" + file + "]";
                    Log.d(TAG, msg);
                    (new Handler(Looper.getMainLooper())).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                    distributeToNetwork(file);
//                    fileSaved = "New photo Saved: " + file;
                }
            }
        };
        observer.startWatching();
    }

    private void distributeToNetwork(String file) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();


        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service done", Toast.LENGTH_SHORT).show();
    }
}