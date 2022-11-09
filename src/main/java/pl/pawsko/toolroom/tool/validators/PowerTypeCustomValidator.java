package pl.pawsko.toolroom.tool.validators;

import pl.pawsko.toolroom.powertype.PowerTypeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PowerTypeCustomValidator implements ConstraintValidator<PowerTypeCustomConstraint, Long> {

    private final PowerTypeRepository powerTypeRepository;

    public PowerTypeCustomValidator(PowerTypeRepository powerTypeRepository) {
        this.powerTypeRepository = powerTypeRepository;
    }

    @Override
    public void initialize(PowerTypeCustomConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {

        return value != null && powerTypeRepository.existsById(value);
    }
}
