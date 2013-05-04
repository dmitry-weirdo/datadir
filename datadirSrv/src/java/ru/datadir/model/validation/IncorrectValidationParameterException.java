package ru.datadir.model.validation;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
 *
 * Исключение, выбрасываемое в случае некорректного значения параметра валидации.
 */
public class IncorrectValidationParameterException extends RuntimeException
{
	public IncorrectValidationParameterException() {
	}
	public IncorrectValidationParameterException(String message) {
		super(message);
	}
	public IncorrectValidationParameterException(String message, Throwable cause) {
		super(message, cause);
	}
	public IncorrectValidationParameterException(Throwable cause) {
		super(cause);
	}
	public IncorrectValidationParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}