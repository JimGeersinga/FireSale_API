package com.firesale.api.service;

import com.firesale.api.exception.ResourceNotFoundException;
import com.firesale.api.model.Auction;
import com.firesale.api.model.AuctionStatus;
import com.firesale.api.model.Category;
import com.firesale.api.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    @DisplayName("Test getCategories Success")
    void getCategories() {
        // Setup mock repository
        var categories = this.getEmptyCategories();
        doReturn(categories).when(categoryRepository).findAll();

        // Execute service call
        var returned = categoryService.getCategories();

        // Assert response
        verify(categoryRepository).findAll();
        Assertions.assertSame(returned, categories, "Incorrect categories returned");
    }

    @Test
    @DisplayName("Test getAvailableCategories Success")
    void getAvailableCategories() {
        // Setup mock repository
        var categories = this.getEmptyCategories();
        doReturn(categories).when(categoryRepository).findByArchived(false);

        // Execute service call
        var returned = categoryService.getAvailableCategories();

        // Assert response
        verify(categoryRepository).findByArchived(false);
        Assertions.assertSame(returned, categories, "Incorrect categories returned");
    }

    @Test
    @DisplayName("Test getArchivedCategories Success")
    void getArchivedCategories() {
        // Setup mock repository
        var categories = this.getEmptyCategories();
        doReturn(categories).when(categoryRepository).findByArchived(true);

        // Execute service call
        var returned = categoryService.getArchivedCategories();

        // Assert response
        verify(categoryRepository).findByArchived(true);
        Assertions.assertSame(returned, categories, "Incorrect categories returned");
    }

    @Test
    @DisplayName("Test getCategoryById Success")
    void getCategoryById() {
        // Setup mock repository
        var category = this.getEmpty(1L);
        doReturn(Optional.of(category)).when(categoryRepository).findById(1L);

        // Execute service call
        var returned = categoryService.getCategoryById(1L);

        // Assert response
        verify(categoryRepository).findById(1L);
        Assertions.assertSame(returned, category, "Incorrect category returned");
    }

    @Test
    @DisplayName("Test getCategoryById Failure")
    void getCategoryByIdFailure() {
        // Setup mock repository
        doReturn(Optional.empty()).when(categoryRepository).findById(1L);
        String expected = String.format("Resource was not found: [No category exists for id: %d]", 1L);
        // Execute service call
        var returned = Assertions.assertThrows(ResourceNotFoundException.class, () ->categoryService.getCategoryById(1L));
        String actual = returned.getMessage();
        // Assert response
        verify(categoryRepository).findById(1L);
        Assertions.assertEquals(expected, actual, "Error message is incorrect");
    }

    @Test
    @DisplayName("Test create Success")
    void create() {
        // Setup mock repository
        var category = this.getEmpty(1L);
        when(categoryRepository.save(any(Category.class))).thenAnswer((answer) -> answer.getArguments()[0]);

        // Execute service call
        var returned = categoryService.create(category);

        // Assert response
        verify(categoryRepository).save(any(Category.class));
        Assertions.assertSame(returned, category, "Incorrect category returned");
    }

    @Test
    @DisplayName("Test updateCategory Success")
    void updateCategory() {
        // Setup mock repository
        var category = this.getEmpty(1L);
        when(categoryRepository.save(any(Category.class))).thenAnswer((answer) -> answer.getArguments()[0]);
        doReturn(Optional.of(category)).when(categoryRepository).findById(1L);

        // Execute service call
        var returned = categoryService.updateCategory(1L, category);

        // Assert response
        verify(categoryRepository).save(any(Category.class));
        Assertions.assertSame(returned, category, "Incorrect category returned");
    }


    @Test
    @DisplayName("Test updateCategory Failure")
    void updateCategoryFailure() {
        // Setup mock repository
        doReturn(Optional.empty()).when(categoryRepository).findById(1L);
        String expected = String.format("Resource was not found: [No category exists for id: %d]", 1L);
        var category = this.getEmpty(1L);
        // Execute service call
        var returned = Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(1L, category));
        String actual = returned.getMessage();
        // Assert response
        verify(categoryRepository).findById(1L);
        Assertions.assertEquals(expected, actual, "Error message is incorrect");
    }

    @Test
    @DisplayName("Test deleteCategory Failure")
    void deleteCategoryFailure() {
        // Setup mock repository
        doReturn(Optional.empty()).when(categoryRepository).findById(1L);
        String expected = String.format("Resource was not found: [No category exists for id: %d]", 1L);
        // Execute service call
        var returned = Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(1L));
        String actual = returned.getMessage();
        // Assert response
        verify(categoryRepository).findById(1L);
        Assertions.assertEquals(expected, actual, "Error message is incorrect");
    }

    @Test
    @DisplayName("Test deleteCategoryFilled Success")
    void deleteCategoryFilled() {
        // Setup mock repository
        var category = this.getFilled();
        when(categoryRepository.save(any(Category.class))).thenAnswer((answer) -> answer.getArguments()[0]);
        doReturn(Optional.of(category)).when(categoryRepository).findById(1L);

        // Execute service call
        categoryService.deleteCategory(1L);

        // Assert response
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Test deleteCategoryEmpty Success")
    void deleteCategoryEmpty() {
        // Setup mock repository
        var category = this.getEmpty(1L);
        doNothing().when(categoryRepository).delete(any(Category.class));
        doReturn(Optional.of(category)).when(categoryRepository).findById(1L);

        // Execute service call
        categoryService.deleteCategory(1L);

        // Assert response
        verify(categoryRepository).delete(any(Category.class));
    }



    private Category getEmpty(Long id)
    {
        Category category = new Category();
        category.setName("test " + id);
        category.setId(id);
        category.setArchived(false);
        category.setAuctions(new ArrayList<>());
        return category;
    }

    private Category getFilled()
    {
        Category category = new Category();
        category.setName("test 1");
        category.setId(1L);
        category.setArchived(false);
        Auction a = new Auction();
        a.setId(1L);
        a.setName("test");
        a.setBids(new ArrayList<>());
        a.setTags(new ArrayList<>());
        a.setEndDate(LocalDateTime.now().plusDays(1));
        a.setStartDate(LocalDateTime.now());
        a.setStatus(AuctionStatus.READY);
        a.setDescription("Description");
        List<Auction> auctions = (Collections.singletonList(a));
        category.setAuctions(auctions);
        return category;
    }



    private List<Category> getEmptyCategories()
    {
        ArrayList<Category> categories = new ArrayList<>();
        for(int i = 1; i <= 10; i++)
        {
            var c = getEmpty((long) i);
            categories.add(c);
        }
        return categories;
    }



}
