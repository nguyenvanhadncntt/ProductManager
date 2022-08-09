package com.product.manager.repository;

import com.product.manager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("Select c from Category c where c.name = :name")
    List<Category> findCategoryByName(@Param("name") String name);
}
