package pl.pawsko.toolroom.rental.validators;

import pl.pawsko.toolroom.tool.ToolRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ToolValidator implements ConstraintValidator<ToolConstraint, Long> {

    private final ToolRepository toolRepository;

    public ToolValidator(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @Override
    public void initialize(ToolConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {

        return value != null && toolRepository.existsById(value);
    }
}
