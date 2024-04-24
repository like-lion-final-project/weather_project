package com.example.demo.cody.repo;

import com.example.demo.cody.entity.ClothsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
@Repository

public interface ClothsCategoryRepository extends JpaRepository<ClothsCategory,Long> {
    Optional<ClothsCategory> findByType(String type);

}
