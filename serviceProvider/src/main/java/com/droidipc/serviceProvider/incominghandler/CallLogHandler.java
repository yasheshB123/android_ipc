package com.droidipc.serviceProvider.incominghandler;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.droidipc.serviceProvider.data.CallInfoModel;
import com.droidipc.serviceProvider.service.CallLogService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CallLogHandler extends IncomingHandler{

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);


        if (!hasPermission(Manifest.permission.READ_CALL_LOG) &&
                !hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        List<CallInfoModel> log = readUserCallLogs();
        if(!log.isEmpty()){
            String logJson = writeLogToFile(log);

            Message response = new Message();
            response.what = CallLogService.CALL_LOG_RESPONSE;
            response.obj = logJson;
            Messenger replyMessenger = msg.replyTo;
            try {
                replyMessenger.send(response);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private List<CallInfoModel> readUserCallLogs() {

        List<CallInfoModel> logs = new ArrayList<>();

        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor mCursor = context.getContentResolver().query(callUri, null, null, null, strOrder);

        while(mCursor.moveToNext()){

            CallInfoModel callInfo = new CallInfoModel();

            String number = mCursor.getString(mCursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            callInfo.setNumber(number);

            String name = mCursor.getString(mCursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            callInfo.setName(name);

            String callDate = mCursor.getString(mCursor.getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long.parseLong(callDate)));
            callInfo.setDate(dateString);

            String callType = mCursor.getString(mCursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            callInfo.setCallType(callType);

            String duration = mCursor.getString(mCursor.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            callInfo.setDuration(duration);

            logs.add(callInfo);
        }
        return logs;
    }

    private String writeLogToFile(List<CallInfoModel> log){
        Gson gson= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Type type =  new TypeToken<List<CallInfoModel>>(){}.getType();
        String strLog = gson.toJson(log,type);

       // Log.e("CALL LOG",strLog);

        File directory =new File(context.getFilesDir(),"Log");
        if(!directory.exists())
            directory.mkdirs();
        File logFile = new File(directory.getAbsoluteFile(),"call_log.txt");
        try {
            FileUtils.writeStringToFile(logFile,strLog,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strLog;
    }

    private boolean hasPermission(String permission){
        return ActivityCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
    }
}
