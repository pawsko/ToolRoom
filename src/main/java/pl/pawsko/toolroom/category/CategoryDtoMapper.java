package pl.pawsko.toolroom.category;

import org.springframework.stereotype.Service;

@Service
class CategoryDtoMapper {
    CategoryDtoResponse map(Category category) {
        CategoryDtoResponse dto = new CategoryDtoResponse();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }

    Category map(CategoryDtoRequest categoryDtoRequest) {
        Category category = new Category();
        category.setCategoryName(categoryDtoRequest.getCategoryName());
        return category;
    }
}
