package pl.pawsko.toolroom.tool.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocationCustomValidator.class)
@Documented
public @interface LocationCustomConstraint {
    String message() default "Location id is null or invalid";
    Class<?>[] groups() default  {};
    Class<? extends Payload>[] payload() default {};
}
