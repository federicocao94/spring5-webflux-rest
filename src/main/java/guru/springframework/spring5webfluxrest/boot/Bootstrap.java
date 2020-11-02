package guru.springframework.spring5webfluxrest.boot;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        saveCategories();
        saveVendors();
        System.out.println("Bootstrap loading finished");
    }


    private void saveCategories() {
        if(categoryRepository.count().block() == 0) {
            Category cat1 = new Category();
            cat1.setDescription("Test category 1");

            categoryRepository.save(
                    Category.builder().description("Test category 1").build()
            ).block();

            categoryRepository.save(
                    Category.builder().description("Test category 2").build()
            ).block();

            System.out.println("categories loaded: " + categoryRepository.count().block());
        }
    }


    private void saveVendors() {
        if(vendorRepository.count().block() == 0) {

            vendorRepository.save(
                    Vendor.builder().firstName("testv1").build()
            ).block();

            vendorRepository.save(
                    Vendor.builder().firstName("testv2").build()
            ).block();

            System.out.println("vendors loaded: " + vendorRepository.count().block());
        }
    }
}
