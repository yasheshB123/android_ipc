package com.droidipc.serviceProvider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

import com.droidipc.serviceProvider.incominghandler.IncomingHandler;

abstract public class BaseService extends Service {

    protected Messenger commandReceiver;

    public BaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(commandReceiver==null){
            synchronized (BaseService.class){
                if(this.commandReceiver==null){
                   IncomingHandler handler = getIncomingHandler();
                   commandReceiver = new Messenger(handler);
                }
            }
        }
        return commandReceiver.getBinder();
    }

    protected abstract IncomingHandler getIncomingHandler();
}


