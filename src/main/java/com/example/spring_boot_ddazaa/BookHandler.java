package com.example.spring_boot_ddazaa;

import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;
import java.util.stream.Stream;

public class BookHandler {


    public ServerResponse getAllBooks(ServerRequest request) {
        return ServerResponse.ok().body(
                List.of(new BookRouteConfig.Book(
                        "book",
                        "author"
                ))
        );
    }

    public ServerResponse getBookByName(ServerRequest serverRequest) {
        String name = serverRequest.pathVariable("name");
        return ServerResponse.ok().body(
                Stream.of(new BookRouteConfig.Book(
                                "book",
                                "author"
                        ))
                        .filter(book -> book.name().equals(name)).findFirst()
        );
    }
}
