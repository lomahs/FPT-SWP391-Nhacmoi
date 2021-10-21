package fpt.swp391.controller;

import fpt.swp391.model.Category;
import fpt.swp391.service.ICategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category/")
public class CategoryController {

    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllCategories() {
        try {
            List<Category> categories = iCategoryService.getListCategories();
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            categories.forEach(category -> data.add(iCategoryService.toJson(category)));
            return data.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable("id") String id) {
        try {
            Category category = iCategoryService.getCategoryById(id);
            if (category != null) {
                Map<String, Object> data = iCategoryService.toJson(category);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Map<String, Object> data) {
        try {
            Category category = iCategoryService.toCategory(data);
            category.getListSong().forEach(song -> song.getCategories().add(category));
            return iCategoryService.saveCategory(category) ? new ResponseEntity<>(HttpStatus.CREATED)
                    : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") String id,
            @RequestBody Map<String, Object> data) {
        try {
            Category category = iCategoryService.getCategoryById(id);
            if (category == null)
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            Category item = iCategoryService.toCategory(data);
            category.setCategory_name(item.getCategory_name());
            // update list song
            category.getListSong().forEach(song -> song.getCategories().remove(category));
            item.getListSong().forEach(song -> song.getCategories().add(category));
            category.setListSong(item.getListSong());

            return iCategoryService.saveCategory(category) ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("id") String id) {
        try {
            return iCategoryService.deleteCategory(id) ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}