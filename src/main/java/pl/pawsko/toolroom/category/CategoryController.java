package pl.pawsko.toolroom.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Categories")
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(description = "Get all categories", summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "List of all categories", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CategoryDtoResponse.class)))})
    public List<CategoryDtoResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get specific category by id",summary = "Get specific category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Category at provided id was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDtoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "The category with the given ID was not found", content = @Content)})
    ResponseEntity<CategoryDtoResponse> getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(description = "Add new category", summary = "Add new category")
    @ApiResponse(responseCode = "201",
            description = "New category has added",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDtoRequest.class))})
    ResponseEntity<CategoryDtoResponse> saveCategory(@RequestBody CategoryDtoRequest categoryDtoRequest) {
        CategoryDtoResponse savedCategory = categoryService.saveCategory(categoryDtoRequest);
        URI savedCategoryUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();
        return ResponseEntity.created(savedCategoryUri).body(savedCategory);
    }

    @PutMapping("/{id}")
    @Operation(description = "Edit specific category by id", summary = "Edit specific category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category successfully updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The category with the given ID was not found",
                    content = @Content)})
    ResponseEntity<?> replaceCategory(@PathVariable Long id, @RequestBody CategoryDtoRequest categoryDtoRequest) {
        return categoryService.replaceCategory(id, categoryDtoRequest)
                .map(c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
