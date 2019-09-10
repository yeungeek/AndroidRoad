package com.yeungeek.jnisample;

/**
 * @author jian.yang
 * @date 2019-09-02
 */

public class SocketHelper {
    public native void startClient(String serverIp,int serverPort);
}
