package org.example.entities;

import io.micronaut.serde.annotation.Serdeable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Serdeable
public class User {
    private static List<User> userList = new ArrayList<>();
    private static Map<String, User> users = new HashMap<>();
    private String username;
    private String password;
    private String email;
    private String name;
    private String address;

    private User(String username, String password, String email, String name, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.address = address;
    }

    static public User registerUser(UserPayload payload) throws Exception {
        if (users.containsKey(payload.getUsername())) {
            throw new Exception("Already exists");
        }
        User user = new User(payload.getUsername(), payload.getPassword(), payload.getEmail(), payload.getName(), payload.getAddress());
        users.put(user.username, user);
        System.out.println("User registered: "+user.getUsername());
        serializeToXML();
        return user;
    }
    static public User loginUser(UserPayload payload) throws Exception {
        if(users.containsKey(payload.getUsername())){
            if(users.get(payload.getUsername()).getPassword().equals(payload.getPassword())){
                return users.get(payload.getUsername());
            }
            throw new Exception("Incorrect password");
        }
        throw new Exception("User does not exist");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void serializeToXML() {
        System.out.println("Serializing user to XML");
        // Create new XML document
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document newDoc = dBuilder.newDocument();
            Element rootElement = newDoc.createElement("dataset");
            newDoc.appendChild(rootElement);

            // Add users to the XML document
            for (User user : users.values()) {
                Element userElement = newDoc.createElement("user");

                Element idElement = newDoc.createElement("id");
                userElement.appendChild(idElement);

                Element nameElement = newDoc.createElement("name");
                nameElement.appendChild(newDoc.createTextNode(user.getName()));
                userElement.appendChild(nameElement);

                Element addressElement = newDoc.createElement("address");
                addressElement.appendChild(newDoc.createTextNode(user.getAddress()));
                userElement.appendChild(addressElement);

                Element emailElement = newDoc.createElement("email");
                emailElement.appendChild(newDoc.createTextNode(user.getEmail()));
                userElement.appendChild(emailElement);

                Element passwordElement = newDoc.createElement("password");
                passwordElement.appendChild(newDoc.createTextNode(user.getPassword()));
                userElement.appendChild(passwordElement);

                Element usernameElement = newDoc.createElement("username");
                usernameElement.appendChild(newDoc.createTextNode(user.getUsername()));
                userElement.appendChild(usernameElement);

                rootElement.appendChild(userElement);
            }

            // Write the content into an XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(newDoc);

            File outputFile = new File("users.xml");
            StreamResult result = new StreamResult(outputFile);

            transformer.transform(source, result);

            System.out.println("XML file saved to " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<User> getUsers(){
        return users.values().stream().map(user -> new User(user.username, null, user.email, user.name, user.address)).toList();
    }
    public static boolean blockUser(String username){
        if(users.containsKey(username)){
            users.remove(username);
            serializeToXML();
            return true;
        }
        return false;
    }
}
