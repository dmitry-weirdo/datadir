package ru.datadir.model.validation;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 *
 * Результат валидации.
 */
public class ValidationResult
{
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	/**
	 * <code>true</code> &mdash; если значение валидно
	 * <br/>
	 * <code>false</code> &mdash; если значение невалидно
	 */
	private boolean valid;

	/**
	 * Список сообщений об ошибках в валидации.
	 * Может быть <code>null</code> или не заполнен, успех валидации определяется
	 * только значением {@link #valid}.
	 */
	private List<String> errorMessages;
}