package pl.pawsko.toolroom.category;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryDtoMapper categoryDtoMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoMapper = categoryDtoMapper;
    }

    public List<CategoryDtoResponse> getAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(categoryDtoMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDtoResponse> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryDtoMapper::map);
    }

    public CategoryDtoResponse saveCategory(CategoryDtoRequest categoryDtoRequest) {
        Category category = categoryDtoMapper.map(categoryDtoRequest);
        Category savedCategory = categoryRepository.save(category);
        return categoryDtoMapper.map(savedCategory);
    }

    public Optional<CategoryDtoResponse> replaceCategory(Long id, CategoryDtoRequest categoryDtoRequest) {
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
