package pl.pawsko.toolroom.tool.validators;

import pl.pawsko.toolroom.category.CategoryRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryCustomValidator implements ConstraintValidator<CategoryCustomConstraint, Long> {

    private final CategoryRepository categoryRepository;

    public CategoryCustomValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void initialize(CategoryCustomConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {

        return value != null && categoryRepository.existsById(value);
    }
}
