package org.example;

import java.io.*;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) throws IOException {
       Socket socket = new Socket("localhost",7667);

       while(true) {
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
           System.out.println("Enter the text: ");
           String line = bufferedReader.readLine();
           if(line.equals("bye")){
               break;
           }

           OutputStream outputStreamtoSocket = socket.getOutputStream();
           PrintWriter printWriter = new PrintWriter(outputStreamtoSocket, true);
           printWriter.println(line);

           InputStream inputStreamFromSocket = socket.getInputStream();
           bufferedReader = new BufferedReader(new InputStreamReader(inputStreamFromSocket));
           String message = bufferedReader.readLine();
           System.out.println("Text: " + line);
           System.out.println("MD5Hash of Text: " + message);

       }
        System.out.println("Closing connection...");
        socket.close();
    }
}
