package com.droidipc.serviceProvider.service;

import com.droidipc.serviceProvider.BaseService;
import com.droidipc.serviceProvider.incominghandler.CallLogHandler;
import com.droidipc.serviceProvider.incominghandler.IncomingHandler;

public class CallLogService extends BaseService {

    public static final int CALL_LOG_RESPONSE =2;

    CallLogHandler callLogHandler;

    @Override
    protected IncomingHandler getIncomingHandler() {
        callLogHandler = new CallLogHandler();
        callLogHandler.setContext(this);
        return callLogHandler;
    }
}
