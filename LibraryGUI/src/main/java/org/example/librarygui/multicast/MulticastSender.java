package org.example.librarygui.multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender {
    public void sendMessage(String address, int port, String message) {
        try {
            // Create a multicast socket
            MulticastSocket socket = new MulticastSocket();

            // Define multicast group and port
            InetAddress group = InetAddress.getByName(address);

            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), group, port);

            socket.send(packet);
            System.out.println("Message sent: " + message);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
