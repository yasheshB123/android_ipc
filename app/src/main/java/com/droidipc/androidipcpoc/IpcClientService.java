package com.droidipc.androidipcpoc;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.droidipc.serviceProvider.service.ImeiService;

import java.util.HashMap;
import java.util.Map;





public class IpcClientService extends Service{


    public static final int ACTION_GET_IMEI = 0;
    //request connections
    private Map<String,Messenger> serviceConnectionRegister;
    //response connection
    private Messenger mMessenger;

    public IpcClientService(){
        serviceConnectionRegister = new HashMap<>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        if(mMessenger==null){
            synchronized (IpcClientService.class){
                if(mMessenger==null){
                    mMessenger = new Messenger(new RequestHandler());
                }
            }
        }

        Intent imeiIntent = new Intent(this, ImeiService.class);
        bindService(imeiIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IpcClientService.this.onServiceConnected(name,service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                IpcClientService.this.onServiceDisconnected(name);
            }
        }, Context.BIND_AUTO_CREATE);
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // bind required service providers.



        return super.onStartCommand(intent, flags, startId);
    }


    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("compname",""+name.getClassName());
        serviceConnectionRegister.put(name.getClassName(),new Messenger(service));
    }


    public void onServiceDisconnected(ComponentName name) {
        serviceConnectionRegister.remove(name.getClassName());
    }


    public void onBindingDied(ComponentName name) {

    }


    public void onNullBinding(ComponentName name) {

    }


    private void getImei(){
        Messenger imeiMessenger = serviceConnectionRegister.get(ImeiService.class.getName());

        if(imeiMessenger!=null){
            Message message = new Message();
            message.replyTo = mMessenger;
            try {
                imeiMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    class RequestHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){

               case ACTION_GET_IMEI:
                        getImei();
                   Log.d("CALL GETIMEI",msg.what+"");
                   break;
               case ImeiService.OPERATION_RESPONSE:
                   Log.d("IMEI RESPONSE",msg.obj+"");
                   break;
           }
        }
    }

}
