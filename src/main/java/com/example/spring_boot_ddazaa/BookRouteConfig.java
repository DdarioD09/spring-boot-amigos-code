package com.example.spring_boot_ddazaa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class BookRouteConfig {

    record Book(String name, String author) {
    }

    //    public RouterFunction<ServerResponse> routerFunction(BookHandler bookHandler) {
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        BookHandler bookHandler = new BookHandler();
        return RouterFunctions.route()
                .GET("/api/v1/books", bookHandler::getAllBooks)
                .GET("/api/v1/book/{name}", bookHandler::getBookByName)
                .build();
    }
}
