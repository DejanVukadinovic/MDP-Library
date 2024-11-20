package org.example.remote;

import org.example.entities.Invoice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AccountingInterface extends Remote {
    double processInvoice(Invoice invoice) throws RemoteException;
}
