package pl.pawsko.toolroom.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public
class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;

    List<CategoryDtoResponse> getAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(categoryDtoMapper::map)
                .collect(Collectors.toList());
    }

    Optional<CategoryDtoResponse> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryDtoMapper::map);
    }

    CategoryDtoResponse saveCategory(CategoryDtoRequest categoryDtoRequest) {
        Category category = categoryDtoMapper.map(categoryDtoRequest);
        Category savedCategory = categoryRepository.save(category);
        return categoryDtoMapper.map(savedCategory);
    }

    Optional<CategoryDtoResponse> replaceCategory(Long id, CategoryDtoRequest categoryDtoRequest) {
        if (!categoryRepository.existsById(id)) {
            return Optional.empty();
        } else {
            Category categoryToUpdate = categoryDtoMapper.map(categoryDtoRequest);
            categoryToUpdate.setId(id);
            Category updatedEntity = categoryRepository.save(categoryToUpdate);
            return Optional.of(categoryDtoMapper.map(updatedEntity));
        }
    }
}
