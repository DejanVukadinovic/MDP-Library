package org.example.customergui;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Scanner;

public class ChatController {
    public ListView userList;
    public TextArea messageBody;
    private String host = "localhost";
    private int port = 5050;
    private SSLContext sslContext;
    String selectedId;

    public void loadUsers(MouseEvent mouseEvent) {
        Security.addProvider(new BouncyCastleProvider());
        String serverCertFile = "server-cert.pem"; // Path to the server's certificate
        try {
            // Load the server's certificate from a PEM file
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            FileInputStream fis = new FileInputStream(serverCertFile);
            X509Certificate serverCertificate = (X509Certificate) certificateFactory.generateCertificate(fis);

            // Create a trust store that trusts the server's certificate
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            trustStore.setCertificateEntry("server-cert", serverCertificate);

            // Generate a self-signed client certificate
            KeyPair clientKeyPair = generateKeyPair();
            X509Certificate clientCertificate = generateSelfSignedCertificate(clientKeyPair);

            // Create a key store containing the client's certificate and private key
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setKeyEntry("client-cert", clientKeyPair.getPrivate(), "password".toCharArray(), new Certificate[]{clientCertificate});

            // Set up SSL context with the client certificate and trust store
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "password".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            // Connect to the server using the client certificate
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port)) {
                sslSocket.startHandshake();
                System.out.println("SSL handshake successful");
                //sslSocket.setTcpNoDelay(true);
                sslSocket.setSoTimeout(0);
                sslSocket.setKeepAlive(true);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                writer.write("users#\n");
                writer.flush();


                String response = reader.readLine();
                System.out.println("Received: " + response);
                String[] users = response.split("#");
                for (String user : users) {
                    userList.getItems().add(user);
                }
                reader.close();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedId = newValue.toString();
            }
        });
    }
    // Generate a key pair for the client
    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // Generate a self-signed certificate for the client
    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        // Set certificate validity period
        Date notBefore = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30); // 30 days back
        Date notAfter = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365); // 1 year forward

        // Build certificate
        X509Certificate certificate = CertUtils.generateV1Certificate(keyPair, "CN=Client", notBefore, notAfter);
        return certificate;
    }

    public void handleMessage(MouseEvent mouseEvent) {
        System.out.println("Selected user: " + selectedId);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port)) {
            sslSocket.startHandshake();
            System.out.println("SSL handshake successful");
            //sslSocket.setTcpNoDelay(true);
            sslSocket.setSoTimeout(0);
            sslSocket.setKeepAlive(true);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            writer.write(selectedId+"#"+messageBody.getText()+"\n");
            writer.flush();


            String response = reader.readLine();
            System.out.println("Received: " + response);
            reader.close();
            writer.close();
        }
     catch (Exception e) {
         e.printStackTrace();
     }
    }
}
