package com.firesale.api.repository;

import com.firesale.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    //    @Query("SELECT c FROM Category c WHERE c.id in :ids")
    Collection<Category> findByIdIn(Collection<Long> ids);

    Collection<Category> findByArchived(Boolean archived);
}
