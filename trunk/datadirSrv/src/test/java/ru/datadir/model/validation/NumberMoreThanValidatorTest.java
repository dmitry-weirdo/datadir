package ru.datadir.model.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.datadir.model.validation.IncorrectValidationParameterException;
import ru.datadir.model.validation.ValidationResult;
import ru.datadir.model.validation.basic.NumberMoreThanValidator;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class NumberMoreThanValidatorTest
{
	@Test
	public void testNoValidationParameters() {
		NumberMoreThanValidator validator = createValidator();

		Integer value = 0;
		exception.expect(IncorrectValidationParameterException.class);
		validator.validate(value);
	}

	@Test
	public void testNonNumberValidationParameter() {
		NumberMoreThanValidator validator = createValidator();

		Integer value = 0;
		Object validationParameter = new Object();
		exception.expect(IncorrectValidationParameterException.class);
		validator.validate(value, validationParameter);
	}

	@Test
	public void testNullValidationParameter() {
		NumberMoreThanValidator validator = createValidator();

		Integer value = 0;
		Object validationParameter = null;
		exception.expect(IncorrectValidationParameterException.class);
		validator.validate(value, validationParameter);
	}

	@Test
	public void testTwoValidationParameters() {
		NumberMoreThanValidator validator = createValidator();

		Integer value = 0;
		Number validationParameter1 = 1;
		Number validationParameter2 = 2;
		exception.expect(IncorrectValidationParameterException.class);
		validator.validate(value, validationParameter1, validationParameter2);
	}

	@Test
	public void testLessThanIntegerValueValidation() {
		NumberMoreThanValidator validator = createValidator();

		Integer value = 0;
		Number validationParameter = 1;
		ValidationResult result = validator.validate(value, validationParameter);

		assertEquals(false, result.isValid());
	}

	@Test
	public void testEqualsIntegerValueValidation() {
		NumberMoreThanValidator validator = createValidator();

		Integer value = 1;
		Number validationParameter = 1;
		ValidationResult result = validator.validate(value, validationParameter);

		assertEquals(false, result.isValid());
	}

	@Test
	public void testMoreThanIntegerValueValidation() {
		NumberMoreThanValidator validator = createValidator();

		Integer value = 2;
		Number validationParameter = 1;
		ValidationResult result = validator.validate(value, validationParameter);

		assertEquals(true, result.isValid());
	}

	@Test
	public void testLessThanDoubleValueValidation() {
		NumberMoreThanValidator validator = createValidator();

		Double value = 0.99d;
		Number validationParameter = 1.0d;
		ValidationResult result = validator.validate(value, validationParameter);

		assertEquals(false, result.isValid());
	}

	@Test
	public void testEqualsDoubleValueValidation() {
		NumberMoreThanValidator validator = createValidator();

		Double value = 0.99d;
		Number validationParameter = 0.99d;
		ValidationResult result = validator.validate(value, validationParameter);

		assertEquals(false, result.isValid());
	}

	@Test
	public void testMoreThanDoubleValueValidation() {
		NumberMoreThanValidator validator = createValidator();

		Double value = 1.01d;
		Number validationParameter = 1;
		ValidationResult result = validator.validate(value, validationParameter);

		assertEquals(true, result.isValid());
	}

	private NumberMoreThanValidator createValidator() {
		return new NumberMoreThanValidator();
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();
}