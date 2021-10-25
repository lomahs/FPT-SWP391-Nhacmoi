package fpt.swp391.service;

import fpt.swp391.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ICategoryService {
    Optional<Category> getCategoryById(String id);

    Category saveCategory(Category category);

    void deleteCategory(String id);

    List<Category> getListCategories();
}