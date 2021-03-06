package in.frisc.distroid;

import android.app.Service;
import android.content.Context;
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

import com.koushikdutta.async.Util;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import co.jasonwyatt.squeaky.Database;
import in.frisc.distroid.database.FilesTable;
import in.frisc.distroid.utils.EncryptionDecryption;
import in.frisc.distroid.utils.FileSplitCombine;
import in.frisc.distroid.utils.Utils2;

public class DistroidService extends Service {
    private static final String TAG = "DISTROID SERVICE";
    FileObserver observer;
    SharedPreferences sharedPreferences;
    String folderPath;
    public static long thresholdSize =  30;
    DistroidServer distroidServer;
    Context mContext;


    @Override
    public void onCreate() {
        mContext = this;
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
                            Utils.isServer = true;
                        }
                    });
                }
                else{

                    (new Handler(Looper.getMainLooper())).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Connected to existing temporary server: "+ Utils.serverIP, Toast.LENGTH_SHORT).show();

                            EventBus.getDefault().postSticky(new Utils2.MessageEvent(1, "hello"));
                            String uri = "http://" + Utils.serverIP + ":" + String.valueOf(Utils.DISTROID_PORT) ;

                            EventBus.getDefault().postSticky(new Utils2.MessageEvent(2, uri));
                            Ion.with(mContext)
                                    .load(uri)
                                    .setLogging(TAG,Log.VERBOSE)
                                    .asString()
                                    .withResponse()
                                    .setCallback(new FutureCallback<Response<String>>() {
                                        @Override
                                        public void onCompleted(Exception e, Response<String> result) {
                                            if(e == null){
                                                Log.w(TAG, result.getResult());

                                                EventBus.getDefault().postSticky(new Utils2.MessageEvent(2, result.getResult()));
                                            }
                                            else{
                                                Log.wtf(TAG, e.toString());

                                                EventBus.getDefault().postSticky(new Utils2.MessageEvent(2, e.toString()));
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                        }
                    });
                }
            }
        },5000);

        File directory = new File(folderPath);
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }

        observer = new FileObserver(folderPath ) { // set up a file observer to watch this directory on sd card
            @Override
            public void onEvent(int event, final String fileName) {

                if((event == FileObserver.CREATE || event == FileObserver.MOVED_TO) && !fileName.equals(".probe")){ // check if its a "create" and not equal to .probe because thats created every time camera is launched

                    final String msg = "Distroid: New file found: " + fileName +  " Initiating distribution algorithm!";
                    final File cur = new File(folderPath + File.separator + fileName);
                    Log.d(TAG, msg + String.valueOf(cur.length()) + String.valueOf(cur.exists())+ String.valueOf(cur.getAbsolutePath()));
//                    if(!sharedPreferences.getBoolean("optimize_automatically", false)){
//                        return;
//                    }
                    File cacheDirectory = new File(folderPath + File.separator + ".cache");
                    if (! cacheDirectory.exists()){
                        cacheDirectory.mkdir();
                        // If you require it to make the entire directory path including parents,
                        // use directory.mkdirs(); here instead.
                    }
                    (new Handler(Looper.getMainLooper())).post(new Runnable() {
                        @Override
                        public void run() {
//
//                            if(cur.length() > thresholdSize * 1024) {
                            try {

                                if(Utils.myDb != null) {
                                    Utils.myDb.insert("INSERT INTO files (path, name, thumbnail, size) VALUES (?,?,?,?)", folderPath, fileName, "NONE", cur.length());
                                }
                                List<String> splitFiles = FileSplitCombine.splitFile(cur, (int) (cur.length() / 30), folderName + File.separator + ".cache");
                                EncryptionDecryption.encryptAllChunks(sharedPreferences.getString("secret_key", "NONE"),splitFiles, folderPath);

                            } catch (Exception e){
                                Toast.makeText(DistroidService.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
//                            }
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                    distributeToNetwork(fileName);
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