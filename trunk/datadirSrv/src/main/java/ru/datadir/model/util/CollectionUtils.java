package ru.datadir.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 04.05.13
 * Time: 18:13
 * To change this template use File | Settings | File Templates.
 */
public class CollectionUtils
{
	public static boolean isEmpty(Collection collection) {
		return collection == null || collection.isEmpty();
	}

	public static <T> List<T> getOneElementList(T value) {
		List<T> result = new ArrayList<>();
		result.add(value);
		return result;
	}
}