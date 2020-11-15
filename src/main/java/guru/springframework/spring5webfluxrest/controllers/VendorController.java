package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping({"/", ""})
    public Flux<Vendor> getVendors() {
        return vendorRepository.findAll();
    }


    @GetMapping({"/{id}"})
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }


    @PostMapping({"/", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }


    @PutMapping("/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }


    @PatchMapping("/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {

        return vendorRepository.findById(id)
                .map(foundVendor -> {
                    if(vendor.getFirstName() != null &&
                       !vendor.getFirstName().equals(foundVendor.getFirstName()) ) {
                        foundVendor.setFirstName(vendor.getFirstName());
                    }
                    if(vendor.getLastName() != null &&
                        !vendor.getLastName().equals(foundVendor.getLastName()) ) {
                        foundVendor.setLastName(vendor.getLastName());
                    }
                    return foundVendor;
                })
                .flatMap(vendorRepository::save);

    }

}
