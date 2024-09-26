package com.usermanagement.controllers;

import com.usermanagement.dto.ApiResponse;
import com.usermanagement.enitiy.CategoryEntity;
import com.usermanagement.exception.ResourceNotFoundException;
import com.usermanagement.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    public CategoryRepo categoryRepo;

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<CategoryEntity>>> getAllCategory() {
        Iterable<CategoryEntity> all = this.categoryRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Categories fetched successfully", all));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<Optional<CategoryEntity>>> getCategoryById(@PathVariable int id) {
        Optional<CategoryEntity> categoryById = this.categoryRepo.findById(id);
        if (categoryById.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Category Found successfully!", categoryById));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Data Not Found"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable int id) {
        Optional<CategoryEntity> category = this.categoryRepo.findById(id);

        if (category.isPresent()) {
            this.categoryRepo.delete(category.get());
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Category deleted successfully", null));
        } else {
            throw new ResourceNotFoundException("Category Not Found");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.success("Category not found", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryEntity>> createCategory(@RequestBody CategoryEntity category) {
        CategoryEntity save = this.categoryRepo.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Category created successfully", save));
    }
}
