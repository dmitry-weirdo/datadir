package ru.datadir.model.validation.basic;

import org.junit.Test;
import ru.datadir.model.validation.ValidationResult;
import ru.datadir.model.validation.Validator;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class NotNullValidatorTest
{
	@Test
	public void testNullValue() {
		NotNullValidator validator = createValidator();
		ValidationResult result = validator.validate(null);

		assertEquals(false, result.isValid());

		List<String> errorMessages = result.getErrorMessages();
		assertEquals(1, errorMessages.size());

		// todo: check result.errorMessages when it will be got from properties file
	}

	@Test
	public void testNotNullObjectValue() {
		NotNullValidator validator = createValidator();
		ValidationResult result = validator.validate( new Object() );

		assertEquals(true, result.isValid());
	}

	private NotNullValidator createValidator() {
		return new NotNullValidator();
	}
}