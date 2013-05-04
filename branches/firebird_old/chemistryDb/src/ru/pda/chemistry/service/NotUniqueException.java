package ru.pda.chemistry.service;

/**
 * User: 1
 * Date: 27.04.2010
 * Time: 1:19:13
 * Исключение, возникающее в случае конфликта создаваемой\изменяемой записи в базе данных с уже существующей.
 */
public class NotUniqueException extends Exception
{
	public NotUniqueException(String message) {
		super(message);
	}
}
