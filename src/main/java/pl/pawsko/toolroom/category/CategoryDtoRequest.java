package pl.pawsko.toolroom.category;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
class CategoryDtoRequest {
    @NotNull
    @Size(min = 3, message = "Category name must contain minimum {min} signs")
    private String categoryName;
}
