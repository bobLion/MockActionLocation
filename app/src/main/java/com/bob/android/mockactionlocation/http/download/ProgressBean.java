package com.bob.android.mockactionlocation.http.download;

/**
 * @package com.bob.android.mockactionlocation.http.download
 * @fileName ProgressBean
 * @Author Bob on 2019/1/3 9:59.
 * @Describe TODO
 */
public class ProgressBean {

    private long bytesRead;
    private long contentLength;
    private boolean done;

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
