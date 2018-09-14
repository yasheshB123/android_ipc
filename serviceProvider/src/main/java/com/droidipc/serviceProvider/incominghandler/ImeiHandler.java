package com.droidipc.serviceProvider.incominghandler;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;


import com.droidipc.serviceProvider.service.ImeiService;

import javax.inject.Inject;

public class ImeiHandler extends IncomingHandler {



    @Override
    public void handleMessage(Message msg) {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        Message response = new Message();
        response.what = ImeiService.OPERATION_RESPONSE;
        response.obj = deviceId;
        Messenger replyMessenger = msg.replyTo;
        try {
            replyMessenger.send(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
