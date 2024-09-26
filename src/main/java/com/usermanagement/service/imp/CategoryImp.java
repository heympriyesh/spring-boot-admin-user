package com.usermanagement.service.imp;

import com.usermanagement.enitiy.CategoryEntity;
import com.usermanagement.repo.CategoryRepo;
import com.usermanagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryImp implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public List<CategoryEntity> getAllCategory() {
        return (List<CategoryEntity>) this.categoryRepo.findAll();
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return this.categoryRepo.save(category);
    }

    @Override
    public Optional<CategoryEntity> getCategoryById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Iterable<CategoryEntity> getAllCategories() {
        return null;
    }

    @Override
    public CategoryEntity updateCategory(Integer id, CategoryEntity category) {
        return null;
    }

    @Override
    public void deleteCategory(Integer id) {

    }
}
