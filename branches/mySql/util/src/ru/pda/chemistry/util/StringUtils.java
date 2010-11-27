/*
 Copyright 2008 SEC "Knowledge Genesis", Ltd.
 http://www.kg.ru, http://www.knowledgegenesis.com
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 25.09.2009 10:05:42$
*/
package ru.pda.chemistry.util;

import java.io.UnsupportedEncodingException;

/**
 * @author pda (25.09.2009 10:05:42)
 * @version $Revision$
 */
public class StringUtils
{
	public static String getConcatenation(StringBuffer sb, String ... strings) {
		sb.delete(0, sb.length());

		for (String s : strings)
			sb.append(s);

		return sb.toString();
	}

	public static String getConcatenation(String ... strings) {
		return getConcatenation(new StringBuffer(), strings);
	}

	public static int getCopeckCount(String s) {
		int i = 0;
		while (i < s.length() && Character.isDigit(s.charAt(i)))
			i++;

		int roubles = Integer.parseInt(s.substring(0, i));

		int j = i + 1; // skip ',' char
		while (j < s.length() && Character.isDigit(s.charAt(j)))
			j++;

		int copeckys = Integer.parseInt(s.substring(i + 1, j));

		return 100 * roubles + copeckys;
	}

	public static String getEncodedString(String str) throws UnsupportedEncodingException {
		return new String(str.trim().getBytes("iso-8859-1"), "cp1251");
	}


	/**
	 * Получает 19-значный номер карты из 16-значного золотокороновского номера.
	 * При этом сначала добавляются недостающие цифры следующим образом:<br/>
	 * 16-значный номер: 96 390  17095320051<br/>
	 * 19-значный номер: 9643906317095320051<br/>
	 * Затем последяя цифра в полученном 19-значном номере заменяется на значение кода Люна,
	 * посчитанное от этого 19-значного номера:<br/>
	 * 19-значный номер:                     9643906317095320051<br/>
	 * 19-значный номер с замененной цифрой: 9643906317095320058 (8 - код Люна от номера 9643906317095320051).
	 *
	 * @param sb StringBuffer для строковых операций.
	 * @param shortNumber 16-значный номер карты.
	 * @return 19-значный номер карты.
	 */
	public static String getCardNumber(StringBuffer sb, String shortNumber) {
		getConcatenation(sb, shortNumber.substring(0, 2), "4", shortNumber.substring(2, 5), "63", shortNumber.substring(5, 16));

		char code = getLuhnCode(sb.toString()); // count luhn code
		sb.delete(sb.length() - 1, sb.length()).append(code); // replace last

		return sb.toString();
	}

	/**
	 * Считает код Люна от заданной строки. Код Люна всегда представляет собой символ, содержащий цифру.
	 *
	 * @param s строка.
	 * @return код Люна от строки.
	 */
	private static char getLuhnCode(String s) {
		int sum = 0;
		int len = s.length();
		int cnt = 0;

		int i, p;

		for (i = len - 2; i >= 0; i--)
		{
			if (cnt % 2 == 0)
			{
				p = 2 * Character.getNumericValue(s.charAt(i));
				if (p > 9)
					p -= 9;

				sum += p;
			}

			cnt++;
		}

		cnt = 0;

		for (i = len - 1; i >= 0; i--)
		{
			if (cnt % 2 == 0)
			{
				p = Character.getNumericValue(s.charAt(i));
				sum += p;
			}

			cnt++;
		}

		sum = sum % 10;
		sum = ((10 - sum) % 10);
		sum += Character.getNumericValue(s.charAt(s.length() - 1));

		String str = Integer.toString(sum);
		return str.charAt(str.length() - 1);
	}
}