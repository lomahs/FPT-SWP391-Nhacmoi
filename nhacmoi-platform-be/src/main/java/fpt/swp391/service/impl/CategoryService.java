package fpt.swp391.service.impl;

import fpt.swp391.model.Category;
import fpt.swp391.repository.CategoryRepository;
import fpt.swp391.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category saveCategory(Category category) {

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {

        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getListCategories() {
        return categoryRepository.findAll();
    }
}