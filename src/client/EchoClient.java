package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public EchoClient() {
        try {
            openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openConnection() throws Exception {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        readMessage();
        sendMessage();
    }

    private void readMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String fromServer = in.readUTF();
                        if (fromServer.equals("/end")) {
                            closeConnection();
                            break;
                        }
                        System.out.println("Server: " + fromServer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    try {
                        String outString = scanner.nextLine();
                        //System.out.println("User: " + outString);
                        out.writeUTF(outString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new EchoClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
