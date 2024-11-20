package org.example;

import org.example.remote.AccountingServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.setProperty("java.security.policy", "resources/rmi_policy.txt");
        try {
            AccountingServer server = new AccountingServer();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("accounting", server);

        }catch (MalformedURLException | RemoteException e){
            throw new RuntimeException(e);}

    }
}