package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;


    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @GetMapping({"/", ""})
    public Flux<Category> getCategories() {
        return categoryRepository.findAll();
    }


    @GetMapping("/{id}")
    public Mono<Category> getCategory(@PathVariable String id) {
        return categoryRepository.findById(id);
    }


    @PostMapping({"/", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }


    @PutMapping("/{id}")
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }


    @PatchMapping("/{id}")
    public Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {
//        Category foundCategory = categoryRepository.findById(id).block();
//
//        if( category.getDescription() != null &&
//                !category.getDescription().equals(foundCategory.getDescription()) ) {
//            foundCategory.setDescription(category.getDescription());
//            return categoryRepository.save(foundCategory);
//        }
//
//        return Mono.just(foundCategory);
        Mono<Category> returnCat = categoryRepository.findById(id)
                .map(foundCategory -> {
                    if( category.getDescription() != null &&
                            !category.getDescription().equals(foundCategory.getDescription()) ) {
                        foundCategory.setDescription(category.getDescription());
                    }
                    return foundCategory;
                })
                .flatMap(categoryRepository::save);

        return returnCat;
    }

}
