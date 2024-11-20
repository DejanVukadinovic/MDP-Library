package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class TestClient {
    public static void main(String[] args){
        System.out.println("Hello World!");
            Scanner scanner = new Scanner(System.in);
            String text;
            while (true) {
                text = scanner.nextLine();
               sendMessage(text);
            }


    }
    public static void sendMessage(String message){
        try(Socket socket = new Socket("localhost", 3000)){
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            while(true){
                String line = in.readLine();
                if(line == null){
                    break;
                }
                System.out.println(line);
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
