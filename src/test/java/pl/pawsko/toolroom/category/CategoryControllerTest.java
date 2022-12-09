package pl.pawsko.toolroom.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = CategoryController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private static final ObjectMapper mapper = new ObjectMapper();


    @Test
    void whenGetAllCategories_thenReturnJsonArray() throws Exception {
        CategoryDtoResponse category1 = new CategoryDtoResponse();
        CategoryDtoResponse category2 = new CategoryDtoResponse();
        category1.setCategoryName("Castorama");
        category2.setCategoryName("Obi");
        List<CategoryDtoResponse> allCategories = Arrays.asList(category1, category2);

        given(categoryService.getAllCategories()).willReturn(allCategories);

        mockMvc.perform(get("/api/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].categoryName", is(category1.getCategoryName())))
                .andExpect(jsonPath("$[1].categoryName", is(category2.getCategoryName())));

        verify(categoryService).getAllCategories();
    }

    @Test
    void whenGetCategoryById_thenReturnValidRequest() throws Exception {
        CategoryDtoResponse categoryRes = new CategoryDtoResponse();
        categoryRes.setId(1L);
        categoryRes.setCategoryName("Castorama");

        given(categoryService.getCategoryById(anyLong())).willReturn(Optional.of(categoryRes));

        mockMvc.perform(get("/api/category/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName", is(categoryRes.getCategoryName())));

        verify(categoryService).getCategoryById(anyLong());
    }

    @Test
    void whenGetByIdButBadType_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/category/{id}", "stringInsteadLong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(categoryService);
    }

    @Test
    void whenGetCategoryByIdButNotExisted_thenReturnNotFound() throws Exception {
        given(categoryService.getCategoryById(anyLong())).willReturn(Optional.empty());

        mockMvc.perform(get("/api/category/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(categoryService).getCategoryById(anyLong());
    }

    @Test
    void whenSaveCategory_thenReturnJson() {
        CategoryDtoRequest categoryReq = new CategoryDtoRequest();
        categoryReq.setCategoryName("Castorama");
        CategoryDtoResponse categoryRes = new CategoryDtoResponse();
        categoryRes.setId(1L);
        categoryRes.setCategoryName("Castorama");

        given(categoryService.saveCategory(categoryReq)).willReturn(categoryRes);

        try {
            mockMvc.perform(post("/api/category")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(categoryReq)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/category/1"))
                    .andExpect(jsonPath("$.categoryName", is(categoryReq.getCategoryName())));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(MethodArgumentNotValidException.class);
        }

        verify(categoryService).saveCategory(categoryReq);
    }

    @Test
    void whenReplaceCategory_thenStatusOk() throws Exception {
        CategoryDtoRequest categoryDtoRequest = new CategoryDtoRequest();
        categoryDtoRequest.setCategoryName("Castorama");
        CategoryDtoResponse categoryDtoResponse = new CategoryDtoResponse();
        categoryDtoResponse.setCategoryName("Castorama");

        given(categoryService.replaceCategory(anyLong(), eq(categoryDtoRequest))).willReturn(Optional.of(categoryDtoResponse));

        mockMvc.perform(put("/api/category/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDtoRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(categoryService).replaceCategory(anyLong(), eq(categoryDtoRequest));
    }

    @Test
    void whenReplaceCategoryNonExistingId_thenReturnNotFound() throws Exception {
        CategoryDtoRequest categoryDtoRequest = new CategoryDtoRequest();
        categoryDtoRequest.setCategoryName("Castorama");

        given(categoryService.replaceCategory(anyLong(), eq(categoryDtoRequest))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/category/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDtoRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(categoryService).replaceCategory(anyLong(), eq(categoryDtoRequest));
    }

    @Test
    void whenReplaceTooShortCategoryName_thenReturnBadRequest() throws Exception {
        CategoryDtoRequest categoryDtoRequest = new CategoryDtoRequest();
        categoryDtoRequest.setCategoryName("Ca");

        mockMvc.perform(put("/api/category/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDtoRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(categoryService);
    }

    @Test
    void whenReplaceCategoryNull_thenReturnBadRequest() throws Exception {
        CategoryDtoRequest categoryDtoRequest = new CategoryDtoRequest();

        mockMvc.perform(put("/api/category/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryDtoRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(categoryService);
    }
}