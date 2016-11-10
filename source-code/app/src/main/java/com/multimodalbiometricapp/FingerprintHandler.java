package com.multimodalbiometricapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by prasang7 on 11/11/16.
 */
@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends
        FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context appContext;

    LocalBroadcastManager broadcaster;

    public String COPA_RESULT = "resu";

    public String COPA_MESSAGE = "mess";

    public FingerprintHandler(Context context) {
        appContext = context;

        broadcaster = LocalBroadcastManager.getInstance(appContext);

    }

    public void startAuth(FingerprintManager manager,
                          FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();


        if (ActivityCompat.checkSelfPermission(appContext,
                Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {
        Toast.makeText(appContext,
                "Authentication error\n" + errString,
                Toast.LENGTH_LONG).show();

        sendResult("auth err");
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        Toast.makeText(appContext,
                "Authentication help\n" + helpString,
                Toast.LENGTH_LONG).show();

        sendResult("auth err");
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(appContext,
                "Authentication failed.",
                Toast.LENGTH_LONG).show();

        sendResult("fail");
    }

    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {

        Toast.makeText(appContext, "Authentication succeeded.", Toast.LENGTH_LONG).show();

        sendResult("success");
    }

    public void sendResult(String message) {
        Intent intent = new Intent(COPA_RESULT);
        if(message != null) intent.putExtra(COPA_MESSAGE, message);
        broadcaster.sendBroadcast(intent);
    }
}
