/*
 Copyright 2010 SEC "Open Code", Ltd.
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.01.2010 14:31:24$
*/
package ru.pda.chemistry.util;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;

public class ValidateUtils
{
	/**
	 * ��������� ������� ������ �������������� ����� �� �������� ������.
	 * � ������ ������ � ������ ������������ ������� � �������� ������ � �������� <code>true</code>, ��������������� ������.
	 * @param str �������� ������.
	 * @param request http-������ ��� ����������� ������.
	 * @param emptyMsg ��� ��������, ������������� � ������ ������ ���� ������ <code>null</code> ������.
	 * @param incorrectMsg ��� ��������, ������������� � ������ ������, ���������� �� ����� �����.
	 * @param nonPositiveMsg ��� ��������, ������������� � ������ ������, ���������� ������������� ���� ������ ���� ����� �����. 
	 * @return �������� ������ �������������� ����� ���� <code>null</code>, ���� ��� �������� ��������� ������.
	 */
	public static Integer validatePositiveInt(String str, HttpServletRequest request, String emptyMsg, String incorrectMsg, String nonPositiveMsg) {
		if (str == null || str.trim().isEmpty())
		{
			request.setAttribute(emptyMsg, true);
			return null;
		}

		try
		{
			Integer value = Integer.parseInt(str.trim());
			if (value <= 0)
			{
				request.setAttribute(nonPositiveMsg, true);
				return null;
			}

			return value;
		}
		catch (NumberFormatException e)
		{
			request.setAttribute(incorrectMsg, true);
			return null;
		}
	}

	/**
	 * ��������� ������� ������ �������������� ����� �� �������� ������.
	 * � ������ ������ � ������ ������������ ������� � �������� ������ � �������� <code>true</code>, ��������������� ������.
	 * @param str �������� ������.
	 * @param request http-������ ��� ����������� ������.
	 * @param emptyMsg ��� ��������, ������������� � ������ ������ ���� ������ <code>null</code> ������.
	 * @param incorrectMsg ��� ��������, ������������� � ������ ������, ���������� �� ����� �����.
	 * @param zeroMsg ��� ��������, ������������� � ������ ������, ���������� ����. 
	 * @param negativeMsg ��� ��������, ������������� � ������ ������, ���������� ������������� ����� �����. 
	 * @return �������� ������ �������������� ����� ���� <code>null</code>, ���� ��� �������� ��������� ������.
	 */
	public static Integer validatePositiveInt(String str, HttpServletRequest request, String emptyMsg, String incorrectMsg, String zeroMsg, String negativeMsg) {
		if (str == null || str.trim().isEmpty())
		{
			request.setAttribute(emptyMsg, true);
			return null;
		}

		try
		{
			Integer value = Integer.parseInt(str.trim());

			if (value == 0)
			{
				request.setAttribute(zeroMsg, true);
				return null;
			}

			if (value < 0)
			{
				request.setAttribute(negativeMsg, true);
				return null;
			}

			return value;
		}
		catch (NumberFormatException e)
		{
			request.setAttribute(incorrectMsg, true);
			return null;
		}
	}

	/**
	 * ��������� ������� ������ ���������������� ����� �� �������� ������.
	 * � ������ ������ � ������ ������������ ������� � �������� ������ � �������� <code>true</code>, ��������������� ������.
	 * @param str �������� ������.
	 * @param request http-������ ��� ����������� ������.
	 * @param emptyMsg ��� ��������, ������������� � ������ ������ ���� ������ <code>null</code> ������.
	 * @param incorrectMsg ��� ��������, ������������� � ������ ������, ���������� �� ����� �����.
	 * @param negativeMsg ��� ��������, ������������� � ������ ������, ���������� ������������� ����� �����.
	 * @return �������� ������ �������������� ����� ���� <code>null</code>, ���� ��� �������� ��������� ������.
	 */
	public static Integer validateNonNegativeInt(String str, HttpServletRequest request, String emptyMsg, String incorrectMsg, String negativeMsg) {
		if (str == null || str.trim().isEmpty())
		{
			request.setAttribute(emptyMsg, true);
			return null;
		}

		try
		{
			Integer value = Integer.parseInt(str.trim());
			if (value < 0)
			{
				request.setAttribute(negativeMsg, true);
				return null;
			}

			return value;
		}
		catch (NumberFormatException e)
		{
			request.setAttribute(incorrectMsg, true);
			return null;
		}
	}


	/**
	 * ��������� ������ �� ���������.
	 * � ������ ������ � ������ ������������ ������� � �������� ������ � �������� <code>true</code>, ��������������� ������.
	 * @param str �������� ������.
	 * @param request http-������ ��� ����������� ������.
	 * @param emptyMsg ��� ��������, ������������� � ������ ������ ���� ������ <code>null</code> ������.
	 * @return ������������� �������� ������ � ������ �������� ������, ���� <code>null</code>, ���� ������ �����.
	 */
	public static String validateString(String str, HttpServletRequest request, String emptyMsg) {
		if (str == null || str.trim().isEmpty())
		{
			request.setAttribute(emptyMsg, true);
			return null;
		}

		return str.trim();
	}

	/**
	 * ��������� ������� ���� � ��������� �������� �� �������� ������.
	 * � ������ ������ � ������ ������������ ������� � �������� ������ � �������� <code>true</code>, ��������������� ������.
	 * @param str �������� ������.
	 * @param format ������ �������� ����.
	 * @param request http-������ ��� ����������� ������.
	 * @param emptyMsg ��� ��������, ������������� � ������ ������ ���� ������ <code>null</code> ������.
	 * @param incorrectMsg ��� ��������, ������������� � ������ ������, �� ���������� ���� � �������� �������.
	 * @return �������� ���� ���� <code>null</code>, ���� ��� �������� ��������� ������.
	 */
	public static Date validateDate(String str, DateFormat format, HttpServletRequest request, String emptyMsg, String incorrectMsg) {
		if (str == null || str.trim().isEmpty())
		{
			request.setAttribute(emptyMsg, true);
			return null;
		}

		String trimmedStr = str.trim();
		ParsePosition position = new ParsePosition(0);
		Date date = format.parse(trimmedStr, position);
		if (position.getIndex() != trimmedStr.length())
		{
			request.setAttribute(incorrectMsg, true);
			return null;
		}

		return date;
	}

	/**
	 * ���������, ������������� �� �������� ��������� � �������� ���� ������� "��������� ���� &lt;= �������� ����".
	 * � ������ ������ � ������ ������������ ������� � �������� ������ � �������� <code>true</code>, ��������������� ������.
	 * @param from ��������� ����.
	 * @param to �������� ����.
	 * @param request http-������ ��� ����������� ������.
	 * @param fromToErrorMsg ��� ��������, ������������� � ������, ���� ��������� ���� ������ ��������.
	 * @return <code>true</code>, ���� ��������� ���� &lt;= �������� ����<br/>
	 * <code>false</code>, ���� ��������� ���� &gt; �������� ����. 
	 */
	public static boolean validateDates(Date from, Date to, HttpServletRequest request, String fromToErrorMsg) {
		if (from.compareTo(to) > 0)
		{
			request.setAttribute(fromToErrorMsg, true);
			return false;
		}

		return true;
	}
}