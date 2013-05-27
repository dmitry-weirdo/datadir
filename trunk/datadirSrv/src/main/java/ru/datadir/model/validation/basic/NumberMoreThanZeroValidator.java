package ru.datadir.model.validation.basic;

import ru.datadir.model.util.CollectionUtils;
import ru.datadir.model.validation.BasicValidator;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 18:45
 * To change this template use File | Settings | File Templates.
 */
public class NumberMoreThanZeroValidator extends BasicValidator<Number>
{
	@Override
	protected boolean isValid(Number value, Object... validationParameters) {
		return ( value != null ) && ( value.intValue() > 0 );
	}

	@Override
	protected List<String> getErrorMessages(Number value, Object... validationParameters) {
		if (value == null)
			return CollectionUtils.getOneElementList("Value cannot be null."); // todo: use locale-dependent properties

		if (value.intValue() <= 0)
			return CollectionUtils.getOneElementList("Value must be more than 0."); // todo: use locale-dependent properties

		return null;
	}
}