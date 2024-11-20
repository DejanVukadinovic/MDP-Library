package org.example;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Main {
    private static Map<String, List<String>> clients  = new HashMap<>();
    public static void register(String id){
        clients.put(id, new ArrayList<>());
    }
    public static void unregister(String id){
        clients.remove(id);
    }
    public static void send(String id, String message){
        List<String> client = clients.get(id);
        if(client != null){
            System.out.println("Sending message to "+id);
            synchronized (client){
                client.add(message);
                client.notify();
            }
        }
    }
    public static List<String> getMessages(String id){
        List<String> client = clients.get(id);
        return client;
    }
    public static List<String> getClients(){
        return List.copyOf(clients.keySet());
    }
    public static void main(String[] args) {
        int port = 5050;
        String keystoreFile = "server-keystore.jks";
        String keystorePassword = "password"; // Keystore password


        try {
            // Load the server keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream keyStoreFile = new FileInputStream(keystoreFile)) {
                keyStore.load(keyStoreFile, keystorePassword.toCharArray());
            }

            // Initialize KeyManagerFactory with the server's key store
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

            // Set up the SSL context with the key manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Create SSL Server Socket
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
            System.out.println("SSL Server is listening on port " + port);

            while (true) {
                // Accept a new client connection
                Socket clientSocket = sslServerSocket.accept();
                clientSocket.setSoTimeout(0);
                clientSocket.setKeepAlive(true);
                // Create a new thread for each client connection
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
                register(clientSocket.getInetAddress().getHostAddress()+":"+clientSocket.getPort());
                Thread deliveryAgent = new Thread(new ClientDeliveryAgent(clientSocket));
                deliveryAgent.start();

                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Get the client's IP address
            String clientIpAddress = clientSocket.getInetAddress().getHostAddress();
            System.out.println("Handling client at IP: " + clientIpAddress);

            // Handle the client communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Received from client " + clientIpAddress + ": " + line);
                String[] parts = line.split("#");
                if (parts[0].compareToIgnoreCase("users")==0) {
                    List<String> clients = Main.getClients();
                    for(String client : clients){
                        writer.write(client+"#");
                    }
                    writer.write("\n");
                    writer.flush();
                } else{
                    Main.send(parts[0], parts[1]);

                }
            }
            // Close resources
            //reader.close();
            //writer.close();
            //clientSocket.close();

            System.out.println("Client " + clientIpAddress + " disconnected.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
class ClientDeliveryAgent implements Runnable{
    private Socket socket;
    private String id;
    ClientDeliveryAgent(Socket socket){
        this.socket = socket;
        this.id = socket.getInetAddress().getHostAddress()+":"+socket.getPort();
    }
    @Override
    public void run() {
        synchronized (Main.getMessages(id)){
        while (true){
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            List<String> messages = Main.getMessages(id);
            System.out.println("Checking for messages for "+id);
            if (messages == null){
                break;
            }
            System.out.println("Delivering message to "+id);

            while(!messages.isEmpty()){
                writer.write(messages.removeFirst());
                writer.newLine();
            }
            writer.flush();
            messages.wait();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            break;
        }
        }
    }
    }
}