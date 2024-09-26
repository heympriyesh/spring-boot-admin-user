package com.usermanagement.repo;

import com.usermanagement.enitiy.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<CategoryEntity, Integer> {
}
