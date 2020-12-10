package com.ipman.seata.tcc.springboot.dubbo.server.seata;

import io.seata.server.Server;

import java.io.IOException;

public class SeataServerStarter {

    static Server server = null;

    /**
     * 启动Seata本地Server
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        server = new Server();
        server.main(args);
    }

}
