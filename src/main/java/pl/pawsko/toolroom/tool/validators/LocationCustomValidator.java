package pl.pawsko.toolroom.tool.validators;

import pl.pawsko.toolroom.location.LocationRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationCustomValidator implements ConstraintValidator<LocationCustomConstraint, Long> {

    private final LocationRepository locationRepository;

    public LocationCustomValidator(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void initialize(LocationCustomConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {

        return value != null && locationRepository.existsById(value);
    }
}
