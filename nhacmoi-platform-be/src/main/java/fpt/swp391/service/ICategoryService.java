package fpt.swp391.service;

import fpt.swp391.model.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Optional<Category> getCategoryById(String id);

    Category saveCategory(Category category);

    void deleteCategory(String id);

    List<Category> getListCategories();
}