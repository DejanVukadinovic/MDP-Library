package org.example.librarygui.multicast;

import java.util.ArrayList;
import java.util.List;

public class MulticastAgent {
    private int port = 5555;
    private static MulticastAgent instance = null;
    private List<String> messages = new ArrayList<>();
    private String multicastAddress = "239.255.0.1";
    private MulticastAgent(int port){
        this.port = port;
        initiateListening();
    }
    public static MulticastAgent getInstance(){
        return getInstance(5555);
    }
    public static MulticastAgent getInstance(int port){
        if(instance == null){
            instance = new MulticastAgent(port);
        }
        return instance;
    }
    private void initiateListening(){
        Thread listener = new Thread(new MulticastListener(multicastAddress, port, this::consumeMessage));
        listener.start();
    }
    private void consumeMessage(String message){
        System.out.println("Received message: " + message);
        messages.add(message);
    }
    public String getMessage(){
        if(messages.isEmpty()){
            return "No requests";
        }
        return messages.removeFirst();
    }
    public void sendMessage(String message) {
        MulticastSender sender = new MulticastSender();
        sender.sendMessage(multicastAddress, port, message);
    }


}
