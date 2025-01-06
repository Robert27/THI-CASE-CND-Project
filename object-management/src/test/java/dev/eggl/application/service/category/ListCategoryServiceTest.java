package dev.eggl.application.service.category;

import dev.eggl.domain.model.Category;
import dev.eggl.port.out.CategoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ListCategoryServiceTest {

    private CategoryPort categoryPort;
    private ListCategoryService listCategoryService;

    @BeforeEach
    public void setUp() {
        categoryPort = Mockito.mock(CategoryPort.class);
        listCategoryService = new ListCategoryService(categoryPort);
    }

    @Test
    public void testFindAll() {
        List<Category> categories = Arrays.asList(
                new Category(1, "Category1", "Description1"),
                new Category(2, "Category2", "Description2")
        );

        when(categoryPort.findAll()).thenReturn(categories);

        List<Category> result = listCategoryService.findAll();
        assertEquals(2, result.size());
        assertEquals("Category1", result.get(0).getName());
    }

    @Test
    public void testExistsById() {
        when(categoryPort.existsById(1)).thenReturn(true);

        Boolean result = listCategoryService.existsById(1);
        assertEquals(true, result);
    }
}
