package com.books.library.controller;

import com.books.library.model.Book;
import com.books.library.model.BookOrder;
import com.books.library.model.User;
import com.books.library.service.BookService;
import com.books.library.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private BookService bookService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setViewResolvers((viewName, locale) -> new org.springframework.web.servlet.view.InternalResourceView("/WEB-INF/views/" + viewName + ".html"))
                .build();
    }

    @Test
    void shouldDenyAccessToHomePageForUnauthorizedUsers() throws Exception {
        mockMvc.perform(get("/order/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void shouldDenyAccessToOrderBookForUnauthorizedUsers() throws Exception {
        mockMvc.perform(post("/order/orderBook")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void shouldDenyAccessToReturnBookForUnauthorizedUsers() throws Exception {
        mockMvc.perform(post("/order/returnBook")
                        .param("orderId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void shouldDenyAccessToExtendLoanForUnauthorizedUsers() throws Exception {
        mockMvc.perform(post("/order/extendLoan")
                        .param("orderId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
