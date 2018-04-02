package in.frisc.distroid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

import in.frisc.distroid.utils.RandomString;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    Context mContext;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

        } else {
            Intent intent = new Intent(this, DistroidService.class);
            startService(intent);
        }

        // Key generation code
        if(sharedPreferences.getString("secret_key","NONE").equals("NONE")){

            Toast.makeText(mContext, "Created secret key!", Toast.LENGTH_SHORT).show();
            RandomString gen = new RandomString(32, new SecureRandom());
            sharedPreferences.edit().putString("secret_key", gen.nextString()).apply();

            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("Key Generated");
            alertDialog.setMessage("Device initiated with Distroid!. Private key generated and stored securely.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        final String folderName = sharedPreferences.getString(getString(R.string.key_backup_directory), "DistroidTest");
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
        String folderPath = android.os.Environment.getExternalStorageDirectory().toString() + "/" + folderName;
        File directory = new File(folderPath);
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        Utils.getNetworkIPs();
    }

    public void openSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsPrefActivity.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, DistroidService.class);
                    startService(intent);
                } else {

//                    finish();
                }
                return;
            }
        }
    }

}
