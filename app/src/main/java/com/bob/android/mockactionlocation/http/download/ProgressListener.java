package com.bob.android.mockactionlocation.http.download;

/**
 * @package com.bob.android.mockactionlocation.http.download
 * @fileName ProgressListener
 * @Author Bob on 2019/1/3 10:00.
 * @Describe TODO
 */
public interface ProgressListener {

    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
