package ru.pda.chemistry.entities.attributeValue;

import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.AttributeType;
import static ru.pda.chemistry.util.StringUtils.getConcatenation;

/**
 * User: 1
 * Date: 05.06.2010
 * Time: 12:30:22
 */
public class StringAttributeValue extends AttributeValue
{
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = (String) value;
	}
	public String getSqlAttributeValue() {
		if (value == null)
			return "null";

		return getConcatenation("'", value, "'");
	}
	public String getHtmlAttributeValue() {
		if (value == null)
			return "-";

		return value;
	}

	public void setAttribute(Attribute attribute) {
		if (attribute.getType() != AttributeType.STRING)
			throw new IllegalArgumentException("Attribute type must be string");

		this.attribute = attribute;
	}

	private String value;
}