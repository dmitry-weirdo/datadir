/**
 * User: 1
 * Date: 30.12.2010
 * Time: 23:42:04
 */
package ru.datadir;

/**
 * ����������, ������������� � ������ ��������� ������ ������-������ ����������.
 */
public class ConstraintException extends Exception
{
	public ConstraintException() {
		super();
	}
	public ConstraintException(Throwable cause) {
		super(cause);
	}
	public ConstraintException(String message, Throwable cause) {
		super(message, cause);
	}
}