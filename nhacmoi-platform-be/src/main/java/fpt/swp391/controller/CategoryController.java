package fpt.swp391.controller;

import fpt.swp391.model.Category;
import fpt.swp391.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<Set<Category>> getAllCategories() {

        Set<Category> categories = new HashSet<>(categoryService.getListCategories());

        return categories.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") String id) {

        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {

        return categoryService.saveCategory(category) ? new ResponseEntity<>(category, HttpStatus.CREATED)
                : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }

    @PutMapping("/edit")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {

        Category cat = categoryService.getCategoryById(category.getCategory_id());
        if (cat == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return categoryService.saveCategory(category) ? new ResponseEntity<>(cat, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") String id) {

        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            return new ResponseEntity<>("Can't find id", HttpStatus.NOT_FOUND);
        }

        return categoryService.deleteCategory(id) ? new ResponseEntity<>("Delete Successful", HttpStatus.OK)
                : new ResponseEntity<>("Can't find id", HttpStatus.NOT_FOUND);
    }
}