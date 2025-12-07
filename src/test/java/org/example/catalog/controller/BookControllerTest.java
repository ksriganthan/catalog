package org.example.catalog.controller;

import org.example.catalog.data.Book;
import org.example.catalog.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRestController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testGetBooks() throws Exception{
        List<String>keywords = new ArrayList<>(Arrays.asList("Fitzek","Nachbar","Mimik"));
        Book book3 = new Book("9783426281758", "Der Nachbar", "Frau leidet an Monophobie, Nachbar verfolgt sie");
        Book book5 = new Book("9783426519486", "Mimik", "Frau leidet an Gedächtnisverlust und versucht den Mord an Paul zu verhindern");

        when(bookService.searchBooks(keywords)).thenReturn(List.of(book3,book5));

        mockMvc.perform(get("/books/search")
                        .param("keyword", "Fitzek", "Nachbar", "Mimik"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Der Nachbar"))
                .andExpect(jsonPath("$[1].title").value("Mimik"));

        verify(bookService).searchBooks(keywords);


    }

    @Test
    void testGetNullBooks() throws Exception {
        List<String> keywords = new ArrayList<>(Arrays.asList("Fitzek", "Spring"));

        when(bookService.searchBooks(keywords)).thenReturn(List.of());

        mockMvc.perform(get("/books/search")
                        .param("keyword", "Fitzek", "Spring"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(bookService).searchBooks(keywords);

    }
}
