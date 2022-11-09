package pl.pawsko.toolroom.tool.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryCustomValidator.class)
@Documented
public @interface CategoryCustomConstraint {
    String message() default "Category id is null or invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
