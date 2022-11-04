package pl.pawsko.toolroom.category;

import org.springframework.stereotype.Service;

@Service
public class CategoryDtoMapper {
    CategoryDto map(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
    return dto;
    }

    Category map(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setCategoryName(categoryDto.getCategoryName());
        return category;
    }
}
