package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Socket socket = null;

    public static void main(String[] args) {
        readMessage();
        writeMessage();
    }

    private static void writeMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String strFromServer = scanner.nextLine();
                    try {
                        //System.out.println("Server: " + strFromServer);
                        out.writeUTF(strFromServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static void readMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket serverSocket = new ServerSocket(8189)) {
                    System.out.println("Server starting...");
                    socket = serverSocket.accept();
                    System.out.println("Client is connect");
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/exit")) {
                            break;
                        }
                        System.out.println("User: " + str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
