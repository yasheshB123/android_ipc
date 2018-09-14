package com.droidipc.serviceProvider.incominghandler;

import android.content.Context;

import java.util.logging.Handler;

import javax.inject.Inject;

public abstract class IncomingHandler extends android.os.Handler {


    protected Context context;

    public void setContext(Context context){
        this.context = context;
    }
}
