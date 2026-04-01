package com.willyb0t.dreamshops.service.category;

import com.willyb0t.dreamshops.exception.AlreadyExistsException;
import com.willyb0t.dreamshops.exception.ResultNotFoundException;
import com.willyb0t.dreamshops.model.Category;
import com.willyb0t.dreamshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
     private final CategoryRepository categoryRepository;
    /**
     * @param id 
     * @return
     */
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new ResultNotFoundException("Category not found!"));
    }

    /**
     * @param name 
     * @return
     */
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * @return 
     */
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * @param category 
     * @return
     */
    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c ->!categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save).orElseThrow(()-> new AlreadyExistsException(category.getName() + "already exists"));
    }

    /**
     * @param category 
     * @return
     */
    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory ->{
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(()-> new ResultNotFoundException("Category not found!"));
    }

    /**
     * @param id 
     */
    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,()-> {
                    throw new ResultNotFoundException("Category not found!");
                });
    }
}
