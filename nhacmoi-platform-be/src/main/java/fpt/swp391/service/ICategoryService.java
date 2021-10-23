package fpt.swp391.service;

import fpt.swp391.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(String id);

    boolean saveCategory(Category category);

    boolean deleteCategory(String id);

    List<Category> getListCategories();
}