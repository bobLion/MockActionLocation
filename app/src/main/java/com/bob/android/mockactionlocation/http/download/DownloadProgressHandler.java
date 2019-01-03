package com.bob.android.mockactionlocation.http.download;

import android.os.Looper;
import android.os.Message;

/**
 * @package com.bob.android.mockactionlocation.http.download
 * @fileName DownloadProgressHandler
 * @Author Bob on 2019/1/3 9:58.
 * @Describe TODO
 */
public abstract class DownloadProgressHandler  extends ProgressHandler{

    private static final int DOWNLOAD_PROGRESS = 1;
    protected ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());

    @Override
    protected void sendMessage(ProgressBean progressBean) {
        mHandler.obtainMessage(DOWNLOAD_PROGRESS,progressBean).sendToTarget();
    }

    @Override
    protected void handleMessage(Message message){
        switch (message.what){
            case DOWNLOAD_PROGRESS:
                ProgressBean progressBean = (ProgressBean)message.obj;
                onProgress(progressBean.getBytesRead(),progressBean.getContentLength(),progressBean.isDone());
        }
    }
}
