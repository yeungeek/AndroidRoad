/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        ReadRequest.java
**
**  PURPOSE     Encapsulate video input parameters
**
**==========================================================================*/

package com.android.avPipe;

import java.nio.ByteBuffer;

/**
 * Read Request used by JNI to pull data from java.
 * <p>
 */
public class ReadRequest {
    private ByteBuffer buffer;
	public ReadRequest(ByteBuffer b) { buffer = b; }
    public ByteBuffer getBuffer() { return buffer; }
}
