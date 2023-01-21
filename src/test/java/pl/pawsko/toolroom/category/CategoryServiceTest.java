package pl.pawsko.toolroom.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private static Category category1;
    private static Category category2;
    private static CategoryDtoResponse categoryRes1;
    private static CategoryDtoResponse categoryRes2;
    private static CategoryDtoRequest categoryReq1;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryDtoMapper categoryDtoMapper;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeAll
    public static void setup() {
        category1 = new Category();
        category1.setId(1L);
        category1.setCategoryName("Castorama");
        category2 = new Category();
        category2.setId(2L);
        category2.setCategoryName("Obi");
        categoryRes1 = new CategoryDtoResponse();
        categoryRes1.setId(1L);
        categoryRes1.setCategoryName("Castorama");
        categoryRes2 = new CategoryDtoResponse();
        categoryRes2.setId(2L);
        categoryRes2.setCategoryName("Obi");
        categoryReq1 = new CategoryDtoRequest();
        categoryReq1.setCategoryName("Castorama");
    }

    @Test
    void shouldReturnTwoCategories() {
        List<Category> categories = List.of(category1, category2);
        List<CategoryDtoResponse> categoriesDtoRes = List.of(categoryRes1, categoryRes2);

        given(categoryRepository.findAll()).willReturn(categories);
        given(categoryDtoMapper.map(category1)).willReturn(categoryRes1);
        given(categoryDtoMapper.map(category2)).willReturn(categoryRes2);

        List<CategoryDtoResponse> allCategoriesActual = categoryService.getAllCategories();

        Assertions.assertNotEquals(categoriesDtoRes.get(0), allCategoriesActual.get(1));
        Assertions.assertIterableEquals(categoriesDtoRes, allCategoriesActual);
        verify(categoryRepository).findAll();
        verify(categoryDtoMapper).map(category1);
        verify(categoryDtoMapper).map(category2);
    }

    @Test
    void shouldReturnOneCategory() {
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category1));
        given(categoryDtoMapper.map(category1)).willReturn(categoryRes1);

        Optional<CategoryDtoResponse> categoryByIdActual = categoryService.getCategoryById(1L);

        Assertions.assertEquals(Optional.of(categoryRes1), categoryByIdActual);
        Assertions.assertNotEquals(Optional.of(categoryRes2), categoryByIdActual);
        verify(categoryRepository).findById(1L);
        verify(categoryDtoMapper).map(category1);
    }

    @Test
    void shouldSaveCategory() {
        given(categoryDtoMapper.map(categoryReq1)).willReturn(category1);
        given(categoryRepository.save(category1)).willReturn(category1);
        given((categoryDtoMapper.map(category1))).willReturn(categoryRes1);

        CategoryDtoResponse categoryDtoResponseActual = categoryService.saveCategory(categoryReq1);

        Assertions.assertEquals(categoryRes1, categoryDtoResponseActual);
        Assertions.assertNotEquals(categoryRes2, categoryDtoResponseActual);
        verify(categoryRepository).save(category1);
        verify(categoryDtoMapper).map(category1);
        verify(categoryDtoMapper).map(categoryReq1);
    }

    @Test
    void shouldReplaceCategoryWhenIdExists() {
        given(categoryDtoMapper.map(categoryReq1)).willReturn(category1);
        given(categoryRepository.save(category1)).willReturn(category1);
        given(categoryDtoMapper.map(category1)).willReturn(categoryRes1);
        given(categoryRepository.existsById(1L)).willReturn(true);

        Optional<CategoryDtoResponse> categoryDtoResponseActual = categoryService.replaceCategory(1L, categoryReq1);

        Assertions.assertEquals(Optional.of(categoryRes1), categoryDtoResponseActual);
        verify(categoryRepository).save(category1);
        verify(categoryDtoMapper).map(category1);
        verify(categoryDtoMapper).map(categoryReq1);
    }

    @Test
    void shouldReplaceEmptyOptionalWhenIdDoesNotExists() {
        given(categoryRepository.existsById(3L)).willReturn(false);

        Optional<CategoryDtoResponse> categoryDtoResponseActual = categoryService.replaceCategory(3L, categoryReq1);

        Assertions.assertEquals(Optional.empty(), categoryDtoResponseActual);
    }
}