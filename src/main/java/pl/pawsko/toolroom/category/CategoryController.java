package pl.pawsko.toolroom.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pawsko.toolroom.hellpers.UriHelper;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Categories")
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(description = "Get all categories")
    @ApiResponse(responseCode = "200", description = "List of all categories", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CategoryDtoResponse.class)))})
    public List<CategoryDtoResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Category at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Category with the given ID was not found", content = @Content)})
    ResponseEntity<CategoryDtoResponse> getCategotyById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new category")
    @ApiResponse(responseCode = "201",
            description = "New category has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDtoRequest.class))})
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<CategoryDtoResponse> saveCategory(@Valid @RequestBody CategoryDtoRequest categoryDtoRequest) {
        CategoryDtoResponse savedCategory = categoryService.saveCategory(categoryDtoRequest);
        URI savedCategoryUri = UriHelper.getUri(savedCategory.getId());
        return ResponseEntity.created(savedCategoryUri).body(savedCategory);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceCategory(@PathVariable Long id, @Valid @RequestBody CategoryDtoRequest categoryDtoRequest) {
        return categoryService.replaceCategory(id, categoryDtoRequest)
                .map(categoryDtoResponse -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
