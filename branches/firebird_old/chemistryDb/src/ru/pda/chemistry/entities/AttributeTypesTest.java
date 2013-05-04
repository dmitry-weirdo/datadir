package ru.pda.chemistry.entities;

/**
 * User: 1
 * Date: 08.05.2010
 * Time: 23:34:38
 */
public class AttributeTypesTest
{
	public static void main(String[] args) {
		AttributeType[] types = AttributeType.values();
		for (AttributeType type : types)
		{
			System.out.println("type: "+ type.toString() + " value: " + type.ordinal());
		}
	}
}
