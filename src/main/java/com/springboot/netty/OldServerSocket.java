package com.springboot.netty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class OldServerSocket {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8090);
        Socket clientSocket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        String request = null;
        while((request = reader.readLine()) != null){
            if("Done".equals(request)){
                break;
            }
            String reponse = "处理逻辑完成";
            writer.print(reponse);
        }
    }
}
