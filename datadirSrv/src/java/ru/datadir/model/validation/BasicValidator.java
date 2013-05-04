package ru.datadir.model.validation;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasicValidator<T> implements Validator<T>
{
	@Override
	public ValidationResult validate(T value, Object... validationParameters) {
		validateValidationParameters(validationParameters);

		ValidationResult result = new ValidationResult();
		result.setValid( isValid(value, validationParameters) );

		List<String> errorMessages = getErrorMessages(value, validationParameters);
		if (errorMessages == null)
			errorMessages = Collections.emptyList(); // prevent return null in errorMessages field

		result.setErrorMessages(errorMessages);

		return result;
	}

	protected void validateValidationParameters(Object... validationParameters) throws IncorrectValidationParameterException {
	}

	protected abstract boolean isValid(T value, Object... validationParameters);

	protected abstract List<String> getErrorMessages(T value, Object... validationParameters);
}