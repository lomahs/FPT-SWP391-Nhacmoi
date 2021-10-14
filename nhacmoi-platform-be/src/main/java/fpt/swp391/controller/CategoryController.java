package fpt.swp391.controller;

import fpt.swp391.model.Category;
import fpt.swp391.repository.CategoryRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category/")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("{id}")
    public Category CreateCategory(@PathVariable("id") String id) {
        return categoryRepository.findById(id).get();
    }

    @GetMapping()
    public List<Category> test() {
        return categoryRepository.findAll();

    }
}