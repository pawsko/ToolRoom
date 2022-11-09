package pl.pawsko.toolroom.rental.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ToolValidator.class)
@Documented
public @interface ToolConstraint {
    String message() default "Tool id is null or invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
