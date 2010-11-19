package ru.pda.chemistry.service;

/**
 * User: 1
 * Date: 01.05.2010
 * Time: 6:53:51
 * Исключение, возникающее при нарушении целостности данных.
 */
public class ConstraintException extends Exception
{
	public ConstraintException(String message) {
		super(message);
	}
}
