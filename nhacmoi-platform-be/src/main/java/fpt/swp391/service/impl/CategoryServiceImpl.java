package fpt.swp391.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.swp391.model.Category;
import fpt.swp391.model.Song;
import fpt.swp391.repository.CategoryRepository;
import fpt.swp391.service.ICategoryService;
import fpt.swp391.service.ISongService;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ISongService iSongService;

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
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            category.getListSong().forEach(song -> song.getCategories().remove(category));
            categoryRepository.save(category);
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public List<Category> getListCategories() {
        return categoryRepository.findAll();
    }

    private List<Map<String, Object>> filterObjectData(String entityName, Category category) {
        List<Map<String, Object>> data = new ArrayList<>();
        switch (entityName) {
        case "song":
            category.getListSong().forEach(song -> {
                Map<String, Object> s = iSongService.toJson(song);
                s.remove("categories");
                data.add(s);
            });
            break;
        default:
            break;
        }
        return data;
    }

    @Override
    public Map<String, Object> toJson(Category category) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("cate_id", category.getCategory_id());
        data.put("cate_name", category.getCategory_name());
        data.put("songs", filterObjectData("song", category));
        return data;
    }

    @Override
    public Category toCategory(Map<String, Object> data) {
        Category category = new Category();
        category.setCategory_id((String) data.get("cate_id"));
        category.setCategory_name((String) data.get("cate_name"));
        List<String> listSongId = (List<String>) data.get("songs");
        category.setListSong(new ArrayList<>());
        listSongId.forEach(id -> {
            Song song = iSongService.getSongById(id);
            category.getListSong().add(song);
        });
        return category;
    }

}
