package ru.datadir.model.validation.basic;

import org.junit.Test;
import ru.datadir.model.validation.ValidationResult;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class NumberMoreThanZeroValidatorTest
{
	@Test
	public void testNullValue() {
		NumberMoreThanZeroValidator validator = createValidator();

		Number value = null;
		ValidationResult result = validator.validate(value);

		assertEquals(false, result.isValid());
	}

	@Test
	public void testZeroIntegerValue() {
		NumberMoreThanZeroValidator validator = createValidator();

		Integer value = 0;
		ValidationResult result = validator.validate(value);

		assertEquals(false, result.isValid());
	}

	@Test
	public void testMinusOneIntegerValue() {
		NumberMoreThanZeroValidator validator = createValidator();

		Integer value = -1;
		ValidationResult result = validator.validate(value);

		assertEquals(false, result.isValid());
	}

	@Test
	public void testPlusOneIntegerValue() {
		NumberMoreThanZeroValidator validator = createValidator();

		Integer value = 1;
		ValidationResult result = validator.validate(value);

		assertEquals(true, result.isValid());
	}

	private NumberMoreThanZeroValidator createValidator() {
		return new NumberMoreThanZeroValidator();
	}
}