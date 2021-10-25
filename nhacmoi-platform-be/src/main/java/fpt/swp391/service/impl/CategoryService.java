package fpt.swp391.service.impl;

import fpt.swp391.model.Category;
import fpt.swp391.model.Song;
import fpt.swp391.repository.CategoryRepository;
import fpt.swp391.service.ICategoryService;
import fpt.swp391.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ISongService songService;

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
        Optional<Category> categoryOptional = getCategoryById(id);

        categoryOptional.ifPresent(
                category -> {
                    Set<Song> listSongs = new HashSet<>(category.getListSong());

                    listSongs.forEach(
                            song -> {
                                song.removeCategory(category);

                                songService.saveSong(song);
                            }
                    );
                    categoryRepository.deleteById(id);
                }
        );

    }

    @Override
    public List<Category> getListCategories() {
        return categoryRepository.findAll();
    }
}