package pl.pawsko.toolroom.tool.validators;

import pl.pawsko.toolroom.status.StatusRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StatusCustomValidator implements ConstraintValidator<StatusCustomConstraint, Long> {

    private final StatusRepository statusRepository;

    public StatusCustomValidator(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void initialize(StatusCustomConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {

        return value != null && statusRepository.existsById(value);
    }
}
