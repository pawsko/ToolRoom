package pl.pawsko.toolroom.tool.validators;

import pl.pawsko.toolroom.manufacturer.ManufacturerRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ManufacturerCustomValidator implements ConstraintValidator<ManufacturerCustomConstraint, Long> {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerCustomValidator(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public void initialize(ManufacturerCustomConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {

        return value != null && manufacturerRepository.existsById(value);
    }
}
