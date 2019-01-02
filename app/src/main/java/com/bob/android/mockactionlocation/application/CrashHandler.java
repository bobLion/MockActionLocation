package com.bob.android.mockactionlocation.application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.bob.android.mockactionlocation.config.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @package com.bob.android.mockactionlocation.application
 * @fileName CrashHandler
 * @Author Bob on 2018/12/21 17:34.
 * @Describe TODO
 */
public class CrashHandler  implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Map<String, String> infos = new HashMap<String, String>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandler(){

    }

    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    public void init(Context context){
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!handlerException(ex) && null != mDefaultHandler){
            mDefaultHandler.uncaughtException(thread,ex);
        }else{
            try{
                Thread.sleep(3000);
                GlobalApplication.getInstance().exit();
            }catch (InterruptedException e){
                Log.e(TAG,"error",e);
            }
            Process.killProcess(Process.myPid());
            System.exit(1);

        }
    }

    private boolean handlerException(Throwable ex){
        if(ex == null){
            return false;
        }

        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext,"很抱歉，程序异常即将退出", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        collectDevInfo(mContext);
        saveCrashInfo2File(ex);
        return true;
    }

    /** collect device info */
    private void collectDevInfo(Context context){
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if(null != pi){
                String versionName = pi.versionName == null? "null": pi.versionName;
                String versionCode = pi.versionCode+"";
                infos.put("versionName",versionName);
                infos.put("versionCode",versionCode);
            }
        }catch (PackageManager.NameNotFoundException ex){
            Log.e(TAG,"an Error occured when collect package info",ex);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for(Field field: fields ){
            try{
                field.setAccessible(true);
                infos.put(field.getName(),field.get(null).toString());
                Log.d(TAG,field.getName()+";"+field.get(null));
            }catch (Exception ex){
                Log.e(TAG,"an Error occured when collect package info",ex);
            }
        }
    }

    /** save crashInfo into file */
    private String saveCrashInfo2File(Throwable ex){
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,String> entry: infos.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key+"=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while(null != cause){
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        try{
            long timeStamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash_"+time+ "_"+timeStamp+".log";
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                String path = AppConfig.CRASH_LOG_PATH;
                File dir = new File(path);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path+fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        }catch (Exception e){
            Log.e(TAG,"an Error occured when write error info ",ex);
        }
        return null;
    }
}
