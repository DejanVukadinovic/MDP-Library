package org.example;

import org.example.entities.Invoice;
import org.example.remote.AccountingInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestClient {
    public static void main(String[] args){
        System.out.println("Hello World!");
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AccountingInterface accounting = (AccountingInterface) registry.lookup("accounting");
            Invoice invoice = new Invoice(new ArrayList<>(List.of(1,2,3)), 2000, new Date());
            double result = accounting.processInvoice(invoice);
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
