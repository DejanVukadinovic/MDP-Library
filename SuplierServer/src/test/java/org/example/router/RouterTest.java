package org.example.router;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {

    @Test
    void registerRoute() {
        Router router = Router.getRouter();
        router.registerRoute("/test", (String s) -> "Hello World");
        assertThrows(IllegalArgumentException.class, () -> router.registerRoute("/test", (String s) -> "Hello World"));
    }

    @Test
    void getHandler() {
        Router router = Router.getRouter();
        router.registerRoute("/test2", (String s) -> "Hello World");
        assertEquals("Hello World", router.getHandler("#/test#gasfa").apply(""));
    }
}