package fpt.swp391.service.impl;

import fpt.swp391.model.Category;
import fpt.swp391.repository.CategoryRepository;
import fpt.swp391.service.ICategoryService;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    ISongService songService;

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public boolean saveCategory(Category category) {
        if (category == null)
            return false;

        categoryRepository.save(category);
        return true;
    }

    @Override
    public boolean deleteCategory(String id) {
        Category category = getCategoryById(id);
        if (category != null) {
//            category.getListSong().forEach(song -> song.getCategories().remove(category));
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public List<Category> getListCategories() {
        return categoryRepository.findAll();
    }
}