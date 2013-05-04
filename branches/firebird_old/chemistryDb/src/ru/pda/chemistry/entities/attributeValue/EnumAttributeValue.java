package ru.pda.chemistry.entities.attributeValue;

import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.AttributeType;

/**
 * User: 1
 * Date: 05.06.2010
 * Time: 12:30:22
 */
public class EnumAttributeValue extends AttributeValue
{
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = (Integer) value;
	}
	public String getSqlAttributeValue() {
		if (value == null)
			return "null";

		return value.toString();
	}
	public String getHtmlAttributeValue() {
		if (value == null)
			return "-";

		return valueStr;	
	}

	public void setAttribute(Attribute attribute) {
		if (attribute.getType() != AttributeType.ENUM)
			throw new IllegalArgumentException("Attribute type must be enum");

		this.attribute = attribute;
	}


	public String getValueStr() {
		return valueStr;
	}
	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}
	
	private Integer value;
	private String valueStr;
}