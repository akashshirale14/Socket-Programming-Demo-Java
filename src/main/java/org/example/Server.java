package org.example;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        //Start server
        List<Socket> allConnectedClients = new ArrayList<>();
        int port = 7667;
        try {
            ServerSocket server = new ServerSocket(port);
            Socket clientSocket = server.accept();
            System.out.println("socket created " + clientSocket);
            allConnectedClients.add(clientSocket);

            Thread socketThread = new Thread(new SocketThread(clientSocket));
            socketThread.start();


        } catch (IOException ex) {
            System.out.println("Exception in SocketServer: " +ex);
        }


    }
}

class SocketThread implements Runnable{

    Socket socket;
    MessageDigest messageDigest;
    public SocketThread(Socket socket){
        this.socket=socket;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public void run(){
        try {
            while (true) {
                System.out.println("here");
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String message = reader.readLine();
                System.out.println("Client Message: "+message);
                if(message == null){
                    break;
                }
                messageDigest.update(message.getBytes(StandardCharsets.UTF_8));
                byte[] digest = messageDigest.digest();
                String mD5hash = DatatypeConverter
                        .printHexBinary(digest).toUpperCase();
                System.out.println("Hash: " +mD5hash);
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream,true);
                printWriter.println(mD5hash);

            }

            }catch (IOException ioException){
            System.out.println("Streaming Exception: " +ioException);
        }finally {
            try {
                System.out.println("Closing server connection...");
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        }
}
