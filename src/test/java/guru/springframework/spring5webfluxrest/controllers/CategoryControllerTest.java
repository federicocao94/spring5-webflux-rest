package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

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

    @Test
    void createTest() {
        BDDMockito.given(categoryRepository.saveAll((any(Publisher.class))))
        .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just( Category.builder().description("test").build() );

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }


    @Test
    void updateTest() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just( Category.builder().description("test").build() );

        webTestClient.put()
                .uri("/api/v1/categories/test")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

}