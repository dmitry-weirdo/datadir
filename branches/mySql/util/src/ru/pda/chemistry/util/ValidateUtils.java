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
	 * Выполняет парсинг целого положительного числа из заданной строки.
	 * В случае ошибки в запрос выставляется атрибут с заданным именем и значеним <code>true</code>, соответствующий ошибке.
	 * @param str исходная строка.
	 * @param request http-запрос для выставления ошибок.
	 * @param emptyMsg имя атрибута, выставляемого в случае пустой либо равной <code>null</code> строки.
	 * @param incorrectMsg имя атрибута, выставляемого в случае строки, содержащей не целое число.
	 * @param nonPositiveMsg имя атрибута, выставляемого в случае строки, содержащей отрицательное либо равное нулю целое число. 
	 * @return значение целого положительного числа либо <code>null</code>, если при парсинге произошла ошибка.
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
	 * Выполняет парсинг целого положительного числа из заданной строки.
	 * В случае ошибки в запрос выставляется атрибут с заданным именем и значеним <code>true</code>, соответствующий ошибке.
	 * @param str исходная строка.
	 * @param request http-запрос для выставления ошибок.
	 * @param emptyMsg имя атрибута, выставляемого в случае пустой либо равной <code>null</code> строки.
	 * @param incorrectMsg имя атрибута, выставляемого в случае строки, содержащей не целое число.
	 * @param zeroMsg имя атрибута, выставляемого в случае строки, содержащей нуль. 
	 * @param negativeMsg имя атрибута, выставляемого в случае строки, содержащей отрицательное целое число. 
	 * @return значение целого положительного числа либо <code>null</code>, если при парсинге произошла ошибка.
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
	 * Выполняет парсинг целого неотрицательного числа из заданной строки.
	 * В случае ошибки в запрос выставляется атрибут с заданным именем и значеним <code>true</code>, соответствующий ошибке.
	 * @param str исходная строка.
	 * @param request http-запрос для выставления ошибок.
	 * @param emptyMsg имя атрибута, выставляемого в случае пустой либо равной <code>null</code> строки.
	 * @param incorrectMsg имя атрибута, выставляемого в случае строки, содержащей не целое число.
	 * @param negativeMsg имя атрибута, выставляемого в случае строки, содержащей отрицательное целое число.
	 * @return значение целого положительного числа либо <code>null</code>, если при парсинге произошла ошибка.
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
	 * Проверяет строку на непустоту.
	 * В случае ошибки в запрос выставляется атрибут с заданным именем и значеним <code>true</code>, соответствующий ошибке.
	 * @param str исходная строка.
	 * @param request http-запрос для выставления ошибок.
	 * @param emptyMsg имя атрибута, выставляемого в случае пустой либо равной <code>null</code> строки.
	 * @return триммированое значение строки в случае непустой строки, либо <code>null</code>, если строка пуста.
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
	 * Выполняет парсинг даты с заданными форматом из заданной строки.
	 * В случае ошибки в запрос выставляется атрибут с заданным именем и значеним <code>true</code>, соответствующий ошибке.
	 * @param str исходная строка.
	 * @param format формат парсинга даты.
	 * @param request http-запрос для выставления ошибок.
	 * @param emptyMsg имя атрибута, выставляемого в случае пустой либо равной <code>null</code> строки.
	 * @param incorrectMsg имя атрибута, выставляемого в случае строки, не содержащей дату в заданном формате.
	 * @return значение даты либо <code>null</code>, если при парсинге произошла ошибка.
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
	 * Проверяет, удовлятворяют ли заданные начальная и конечная даты условию "начальная дата &lt;= конечной даты".
	 * В случае ошибки в запрос выставляется атрибут с заданным именем и значеним <code>true</code>, соответствующий ошибке.
	 * @param from начальная дата.
	 * @param to конечная дата.
	 * @param request http-запрос для выставления ошибок.
	 * @param fromToErrorMsg имя атрибута, выставляемого в случае, если начальная дата больше конечной.
	 * @return <code>true</code>, если начальная дата &lt;= конечной дате<br/>
	 * <code>false</code>, если начальная дата &gt; конечной даты. 
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