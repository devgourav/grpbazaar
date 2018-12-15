package com.devworkxlabs.grpbazaarapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.info.internal.ImagePerfImageOriginListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.images.internal.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore.TrustedCertificateEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    String ShowOrHideWebViewInitialUse = "show";
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        new NetworkCheck(this).execute(this);

        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.grpBazaarWebView);
        final String webViewUrlHome = "https://www.grpbazaar.com";
        final String webViewUrlShop = "https://www.grpbazaar.com/shop";
        final Context context = this;


        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        FloatingActionButton callAction = new FloatingActionButton(getBaseContext());
        callAction.setTitle("Call Us");
        callAction.setColorNormalResId(R.color.background);
        callAction.setColorPressedResId(R.color.white_pressed);
        callAction.setIcon(R.drawable.ic_phone_black_24dp);
        callAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9158920775"));
                startActivity(intent);
            }
        });

        FloatingActionButton shopAction = new FloatingActionButton(getBaseContext());
        shopAction.setTitle("Shop");
        shopAction.setColorNormalResId(R.color.background);
        shopAction.setColorPressedResId(R.color.white_pressed);
        shopAction.setIcon(R.drawable.ic_shopping_basket_black_24dp);
        shopAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.loadUrl(webViewUrlShop);
                webview.setWebViewClient(new CustomWebViewClient(context));
            }
        });

        FloatingActionButton rateAction = new FloatingActionButton(getBaseContext());
        rateAction.setTitle("Rate Us");
        rateAction.setColorNormalResId(R.color.background);
        rateAction.setColorPressedResId(R.color.white_pressed);
        rateAction.setIcon(R.drawable.ic_rate_review_black_24dp);
        rateAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.devworkxlabs.grpbazaarapp&hl=en"));
                intent.setPackage("com.android.vending");
                startActivity(intent);
            }
        });
        menuMultipleActions.addButton(shopAction);
        menuMultipleActions.addButton(rateAction);
        menuMultipleActions.addButton(callAction);
        menuMultipleActions.setVisibility(View.INVISIBLE);




        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(TRUE);

        webview.loadUrl(webViewUrlHome);
        webview.setWebViewClient(new CustomWebViewClient(this));
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }

    }
}
