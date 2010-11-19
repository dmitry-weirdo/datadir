package ru.pda.chemistry.entities.attributeValue;

import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.AttributeType;

/**
 * User: 1
 * Date: 05.06.2010
 * Time: 12:30:22
 */
public class IntegerAttributeValue extends AttributeValue
{
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = (Long) value;
	}
	public String getSqlAttributeValue() {
		if (value == null)
			return "null";

		return value.toString();
	}
	public String getHtmlAttributeValue() {
		if (value == null)
			return "-";

		return value.toString();
	}

	public void setAttribute(Attribute attribute) {
		if (attribute.getType() != AttributeType.INTEGER)
			throw new IllegalArgumentException("Attribute type must be integer");

		this.attribute = attribute;
	}

	private Long value;
}