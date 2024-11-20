package org.example;

import com.fasterxml.jackson.core.io.BigIntegerParser;
import com.google.gson.Gson;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.example.entities.User;
import org.example.entities.UserPayload;

@Controller("/user")
public class UserController {

        @Get(uri = "/hello", produces = "text/plain")
        public String index() {
            return "hello world";
        }
        @Post(uri = "/login", produces = "text/plain", consumes = "application/json")
        public String login(@Body UserPayload body) {
            System.out.println(body.getName());
            System.out.println(body.getPassword());
            try {
                System.out.println(User.loginUser(body));
            }catch (Exception e){
                System.out.println(e.getMessage());
                return e.getMessage();
            }
            return "ok";
        }
        @Post(uri = "/register", produces = "text/plain", consumes = "application/json")
        public String register(@Body UserPayload body){
            try{
                System.out.println(User.registerUser(body));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            return "ok";
        }
        @Get(uri = "/", produces = "application/json")
        public String getUsers(){
            Gson gson = new Gson();
            return gson.toJson(User.getUsers());
        }
        @Get(uri = "block/{username}", produces = "application/json")
        public String getUser(String username){
            if(User.blockUser(username)){
                return "ok";
            }
            return "error";
        }
}
