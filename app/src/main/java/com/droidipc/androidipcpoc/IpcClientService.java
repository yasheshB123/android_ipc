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

import com.droidipc.serviceProvider.service.CallLogService;
import com.droidipc.serviceProvider.service.ImeiService;

import java.util.HashMap;
import java.util.Map;

public class IpcClientService extends Service implements  ServiceConnection{


    public static final int ACTION_GET_IMEI = 0;
    public static final int ACTION_READ_CALL_LOG =1;
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

        bindService(new Intent(this, ImeiService.class),this, Context.BIND_AUTO_CREATE);
        bindService(new Intent(this, CallLogService.class),this, Context.BIND_AUTO_CREATE);
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // bind required service providers.
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("compname",""+name.getClassName());
        serviceConnectionRegister.put(name.getClassName(),new Messenger(service));
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        serviceConnectionRegister.remove(name.getClassName());
    }

    @Override
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

    private void readCallLog(){
        Messenger callLogMessenger = serviceConnectionRegister.get(CallLogService.class.getName());

        if(callLogMessenger != null){
            Message message = new Message();
            message.replyTo = mMessenger;
            try {
                callLogMessenger.send(message);
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
               case ACTION_READ_CALL_LOG:
                   readCallLog();
                   break;
               case CallLogService.CALL_LOG_RESPONSE:
                   Log.d("Call Log RESPONSE",msg.obj+"");
                   break;
           }
        }
    }
}
