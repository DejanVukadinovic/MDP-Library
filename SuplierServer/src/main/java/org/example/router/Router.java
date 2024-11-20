package org.example.router;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class Router {
    private static Router router;
    private Map<String, UnaryOperator<String>> routes = new HashMap<>();
    private Router(){}
    public static Router getRouter(){
        if(router == null){
            router = new Router();
        }
        return router;
    }
    public void registerRoute(String path, UnaryOperator<String> handler){
        // Register the route
        if(routes.containsKey(path)){
            throw new IllegalArgumentException("Route already exists");
        }
        routes.put(path, handler);
    }
    public UnaryOperator<String> getHandler(String rawPath){
        // Get the handler for the route
        String path = getPath(rawPath);
        if(!routes.containsKey(path)){
            return (String s) -> "Not found";
        }
        return routes.get(path);
    }

    public String getResponse(String rawPath){
        try{
        System.out.println("getting: " + getPath(rawPath));
        System.out.println("Return: " + getHandler(rawPath).apply(getPayload(rawPath)));
        return getHandler(rawPath).apply(getPayload(rawPath));}
        catch (Exception e){
            System.out.println(e.getMessage());
            return "Not found";
        }
    }

    private static String getPath(String rawPath) {
        return rawPath.split("#")[1];
    }
    private static String getPayload(String rawPath) {
        return rawPath.split("#")[2];
    }
}
