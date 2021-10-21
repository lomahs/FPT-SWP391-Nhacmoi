package fpt.swp391.service;

import java.util.List;
import java.util.Map;

import fpt.swp391.model.Category;

public interface ICategoryService {
    Category getCategoryById(String id);

    boolean saveCategory(Category category);

    boolean deleteCategory(String id);

    List<Category> getListCategories();

    Map<String, Object> toJson(Category category);

    Category toCategory(Map<String, Object> data);
}
