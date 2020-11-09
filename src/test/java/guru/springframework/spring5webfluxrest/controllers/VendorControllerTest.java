package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

public class VendorControllerTest {

    WebTestClient webTestClient;

    VendorRepository vendorRepository;

    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }


    @Test
    void getVendorsTest() {
        BDDMockito.given(vendorRepository.findAll()).willReturn(
                Flux.just(Vendor.builder().firstName("test1").build(),
                        Vendor.builder().firstName("test2").build())
        );

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }


    @Test
    void getVendorByIdTest() {
        BDDMockito.given(vendorRepository.findById("test")).willReturn(
                Mono.just(Vendor.builder().firstName("test1").build())
        );

        webTestClient.get()
                .uri("/api/v1/vendors/test")
                .exchange()
                .expectBody(Vendor.class);
    }


    @Test
    void createTest() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn( Flux.just( Vendor.builder().build() ) );

        Mono<Vendor> vendorToSave = Mono.just( Vendor.builder().firstName("test").build() );

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateTest() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn( Mono.just( Vendor.builder().build() ) );

        Mono<Vendor> vendorToSave = Mono.just( Vendor.builder().firstName("test").build() );

        webTestClient.put()
                .uri("/api/v1/vendors/test")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
