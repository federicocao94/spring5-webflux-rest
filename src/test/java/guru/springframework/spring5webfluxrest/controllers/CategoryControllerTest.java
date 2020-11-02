package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class CategoryControllerTest {

    WebTestClient webTestClient;

    CategoryRepository categoryRepository;

    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void getCategories() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder().description("cat1").build(),
                        Category.builder().description("cat2").build()
                ));

        webTestClient.get()
                .uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getCategory() {
        BDDMockito.given(categoryRepository.findById("test"))
                .willReturn(Mono.just(Category.builder().description("cat1").build()));

        webTestClient.get()
                .uri("/api/v1/categories/test")
                .exchange()
                .expectBody(Category.class);
    }
}