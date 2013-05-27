package ru.datadir.model.validation;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 17:55
 * To change this template use File | Settings | File Templates.
 *
 * Наиболее общий интерфейс валидации значения.
 */
public interface Validator<T>
{
	/**
	 * @param value значение
	 * @return результат валидации
	 */
	ValidationResult validate(T value, Object... validationParameters);
}