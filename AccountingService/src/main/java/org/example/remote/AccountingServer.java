package org.example.remote;

import org.example.entities.Invoice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AccountingServer extends UnicastRemoteObject implements AccountingInterface {
    public AccountingServer() throws RemoteException {
        super();
    }

    public double processInvoice(Invoice invoice) throws RemoteException {
        String filename = invoice.hashCode() + ".ser";
        System.out.println("Saving invoice to " + filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(invoice);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoice.getTotal() * 0.17;
    }
}
