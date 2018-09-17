package com.droidipc.androidipcpoc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_dashboard);

        findViewById(R.id.buttonImei).setOnClickListener((view)->{
            Intent imeiIntent = new Intent(this,IpcClientService.class);
            ServiceConnection connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Messenger messenger = new Messenger(service);
                    try {
                        Message msg = new Message();
                        msg.what = IpcClientService.ACTION_GET_IMEI;
                        messenger.send(msg);
                        Log.d("CALLED","REMOTE");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.w("CLIENT REMOTE",e+"");
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(imeiIntent,connection, Context.BIND_AUTO_CREATE);
            Log.d("CALLED","BINDSERVICE");
        });

        findViewById(R.id.buttonCallLogs).setOnClickListener((view) -> {
            Intent callLogIntent = new Intent(this,IpcClientService.class);
            ServiceConnection connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Messenger messenger = new Messenger(service);
                    try {
                        Message msg = new Message();
                        msg.what = IpcClientService.ACTION_READ_CALL_LOG;
                        messenger.send(msg);
                        Log.d("CALLED CALL LOG","REMOTE");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.w("CLIENT CALL LOG REMOTE",e+"");
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                }
            };
            bindService(callLogIntent,connection,Context.BIND_AUTO_CREATE);
        });

    }

}
