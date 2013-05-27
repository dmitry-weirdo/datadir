package ru.datadir.model.validation.basic;

import ru.datadir.model.util.CollectionUtils;
import ru.datadir.model.validation.BasicValidator;
import ru.datadir.model.validation.IncorrectValidationParameterException;

import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */
public class NumberMoreThanValidator extends BasicValidator<Number>
{
	@Override
	protected boolean isValid(Number value, Object... validationParameters) {
		return ( value != null ) && ( value.floatValue() > getMoreThanParameter(validationParameters).floatValue() );
	}
	@Override
	protected List<String> getErrorMessages(Number value, Object... validationParameters) {
		if (value == null)
			return CollectionUtils.getOneElementList("Value cannot be null."); // todo: use locale-dependent properties

		float moreThanValue = getMoreThanParameter(validationParameters).floatValue();

		if (value.intValue() <= moreThanValue)
			return CollectionUtils.getOneElementList( concat("Value must be more than ", moreThanValue, ".") ); // todo: use locale-dependent properties

		return null;
	}

	@Override
	protected void validateValidationParameters(Object... validationParameters) throws IncorrectValidationParameterException {
		if (validationParameters.length != VALIDATION_PARAMETERS_COUNT)
			throw new IncorrectValidationParameterException( concat("Incorrect validation parameters count (expected count: ", VALIDATION_PARAMETERS_COUNT, ", passed count: ", validationParameters.length, ")" ) );

		Object parameter = validationParameters[0];
		if ( !(parameter instanceof Number) )
			throw new IncorrectValidationParameterException( concat("Incorrect validation parameter: ", parameter, "(Number value is expected)" ) );
	}

	protected Number getMoreThanParameter(Object... validationParameters) {
		return (Number) validationParameters[0];
	}

	public static final int VALIDATION_PARAMETERS_COUNT = 1;
}