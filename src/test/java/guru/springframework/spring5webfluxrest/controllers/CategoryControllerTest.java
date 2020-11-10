package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
        given(categoryRepository.findAll())
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
        given(categoryRepository.findById("test"))
                .willReturn(Mono.just(Category.builder().description("cat1").build()));

        webTestClient.get()
                .uri("/api/v1/categories/test")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void createTest() {
        given(categoryRepository.saveAll((any(Publisher.class))))
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
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just( Category.builder().description("test").build() );

        webTestClient.put()
                .uri("/api/v1/categories/test")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }


    @Test
    void patchTest() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just( Category.builder().description("test").build() );

        webTestClient.patch()
                .uri("/api/v1/categories/test")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());
    }


    @Test
    void patchNoChangesTest() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just( Category.builder().build() );

        webTestClient.patch()
                .uri("/api/v1/categories/test")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any());
    }

}