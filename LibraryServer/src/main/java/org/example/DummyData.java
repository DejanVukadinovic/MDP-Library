package org.example;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import com.google.gson.Gson;


@Serdeable
public class DummyData {
    private String message;
    private String name;
    public DummyData(String message) {
        this.message = message;
    }
    public DummyData(String message, String name) {
        this.message = message;
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
