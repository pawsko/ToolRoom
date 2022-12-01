package pl.pawsko.toolroom.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoMapperTest {



    @Test
    void testMapFromCategoryToCategoryDtoResponse() {

        CategoryDtoMapper categoryDtoMapper = new CategoryDtoMapper();
        Category category = new Category();
        category.setId(1234L);
        category.setCategoryName("AnyCategory");
        CategoryDtoResponse mapped = categoryDtoMapper.map(category);
        assertEquals(mapped.getId(), category.getId());
        assertEquals(mapped.getCategoryName(), category.getCategoryName());
        assertNotEquals(mapped.getCategoryName(), "anyCategory");
        assertNotEquals(mapped.getId(), 4321L);
    }

    @Test
    void testMapFromCategoryDtoRequestToCategory() {
        CategoryDtoMapper categoryDtoMapper = new CategoryDtoMapper();
        CategoryDtoRequest categoryDtoRequest = new CategoryDtoRequest();
        categoryDtoRequest.setCategoryName("RequestCategory");
        Category mapped = categoryDtoMapper.map(categoryDtoRequest);
        assertEquals(mapped.getCategoryName(), categoryDtoRequest.getCategoryName());
        assertNotEquals(mapped.getCategoryName(), "anyCategory");
    }
}