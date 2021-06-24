/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        DataBean.java
**
**  PURPOSE     Encapsulate a media data buffer
**
**==========================================================================*/

package com.android.avPipe;

import java.nio.ByteBuffer;

public class DataBean {
    private ByteBuffer buffer;
    private long ts;
    private boolean isSyncFrame;

    public DataBean(ByteBuffer buffer, long ts, boolean isSyncFrame) {
        super();
        this.buffer = buffer;
        this.ts = ts;
        this.isSyncFrame = isSyncFrame;
    }

    public ByteBuffer getBuffer()            { return buffer; }
    public void setBuffer(ByteBuffer buffer) { this.buffer = buffer; }

    public long getTs()        { return ts; }
    public void setTs(long ts) { this.ts = ts; }

    public boolean isSyncFrame()           { return isSyncFrame; }
    public void setSyncFrame(boolean sync) { this.isSyncFrame = sync; }
}
