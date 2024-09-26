package com.usermanagement.service;

import com.usermanagement.enitiy.CategoryEntity;

import java.util.Optional;

public interface CategoryService {

    CategoryEntity createCategory(CategoryEntity category);


    Optional<CategoryEntity> getCategoryById(Integer id);

    Iterable<CategoryEntity> getAllCategories();

    CategoryEntity updateCategory(Integer id, CategoryEntity category);

    void deleteCategory(Integer id);
}
