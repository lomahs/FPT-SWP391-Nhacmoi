package fpt.swp391.service;

import java.util.List;

import fpt.swp391.model.Category;

public interface ICategoryService {
    Category getCategoryById(String id);

    boolean saveCategory(Category Category);

    boolean deleteCategory(String id);

    List<Category> getListCategories();
}
