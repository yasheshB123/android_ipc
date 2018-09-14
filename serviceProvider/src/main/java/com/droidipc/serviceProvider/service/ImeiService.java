package com.droidipc.serviceProvider.service;

import com.droidipc.serviceProvider.BaseService;


import com.droidipc.serviceProvider.incominghandler.ImeiHandler;
import com.droidipc.serviceProvider.incominghandler.IncomingHandler;



public class ImeiService extends BaseService {

    public static final int OPERATION_RESPONSE = 7855;


    ImeiHandler handler;


    @Override
    protected IncomingHandler getIncomingHandler() {
        handler = new ImeiHandler();
        handler.setContext(this);
        return handler;
    }
}
