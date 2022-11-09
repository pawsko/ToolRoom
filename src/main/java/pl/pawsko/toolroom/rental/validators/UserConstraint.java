package pl.pawsko.toolroom.rental.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidator.class)
@Documented
public @interface UserConstraint {
    String message() default "User id is null or invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
