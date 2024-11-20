package org.example.librarygui.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.function.Consumer;

public class MulticastListener implements Runnable {
    private final String multicastAddress;
    private final int port;
    private volatile boolean running = true;
    private MulticastSocket socket;
    private Consumer<String> messageConsumer;

    public MulticastListener(String multicastAddress, int port, Consumer<String> messageConsumer) {
        this.multicastAddress = multicastAddress;
        this.port = port;
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void run() {
        try {
            InetAddress group = InetAddress.getByName(multicastAddress);
            socket = new MulticastSocket(port);
            socket.joinGroup(group);
            System.out.println("Joined multicast group. Listening for messages...");

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Continuous listening
            while (running) {
                socket.receive(packet);  // Receive a packet
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message: " + message);
                messageConsumer.accept(message);
            }

            // Leaving the group and closing the socket
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to stop the listener
    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            try {
                InetAddress group = InetAddress.getByName(multicastAddress);
                socket.leaveGroup(group);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

