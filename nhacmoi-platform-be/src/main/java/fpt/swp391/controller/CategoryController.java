package fpt.swp391.controller;

import fpt.swp391.model.Category;
import fpt.swp391.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/category/")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("create/{name}")
    public void CreateCategory(@PathVariable("name") String name){
        Category cat = new Category(name);

        categoryRepository.save(cat);
    }
}