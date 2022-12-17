package softuni.exam.util.impl;


import org.springframework.stereotype.Component;
import softuni.exam.util.ValidatorUtil;

import javax.validation.Validation;
import javax.validation.Validator;

@Component
public class ValidatorUtilImpl implements ValidatorUtil {
    private final Validator validator;

    public ValidatorUtilImpl() {
        this.validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Override
    public <E> boolean isValid(E entity) {
        return validator
                .validate(entity)
                .isEmpty();
    }
}
