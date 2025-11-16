package org.example.catalog.controller;

import org.example.catalog.PostgresContainerTest;
import org.example.catalog.data.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookControllerIntegrationTest extends PostgresContainerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setup() {
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Test
    void testSearchBooks() {
        // Anfrage an den echten Server
        ResponseEntity<Book[]> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/search")
                        .queryParam("keyword", "Fitzek", "Mimik")
                        .build())
                .retrieve()// Führt die Anfrage wirklich aus
                .toEntity(Book[].class); // Die JSON-Antwort wird immer in ein Book-Array gepackt

        List<Book>list = new ArrayList<>(Arrays.asList(response.getBody()));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(list).isNotEmpty();
        assertThat(list.stream().anyMatch(b -> b.getTitle().contains("Mimik"))).isTrue();
    }

    @Test
    void testSearchBooksNull() {
        // Anfrage an den echten Server
        ResponseEntity<Book[]> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/search")
                        .queryParam("keyword", "Fitzek", "Spring")
                        .build())
                .retrieve()// Führt die Anfrage wirklich aus
                .toEntity(Book[].class); // Die JSON-Antwort wird immer in ein Book-Array gepackt

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }
}
