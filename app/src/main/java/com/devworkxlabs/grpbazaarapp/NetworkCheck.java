package com.devworkxlabs.grpbazaarapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AlertDialog;

public class NetworkCheck extends AsyncTask<Context, Void, Boolean> {
    private static final String LOG_TAG = "networkCheck" ;
    private static final Integer SETTINGS_REQUEST_CODE = 1 ;
    Context context;

    public NetworkCheck(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Context...contexts) {
        if (isNetworkAvailable(contexts[0])) {
            HttpURLConnection urlc = null;
            try {
                urlc = (HttpURLConnection) (new URL("https://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isActiveNetwork) {
        super.onPostExecute(isActiveNetwork);
        if(isActiveNetwork){
            Log.d(LOG_TAG,"Active Network Connection");
        }else{
            Log.d(LOG_TAG,"InActive Network Connection");
            createAlertDialog();
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("GrpBazaar App requires a active internet connection to open.Please switch on your mobile data or WIFI.")
                .setTitle("No Internet Connection")
                .setIcon(R.drawable.ic_launcher_icon);
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), SETTINGS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finishAffinity();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((Activity)context).finishAffinity();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


