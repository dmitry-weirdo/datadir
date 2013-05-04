package ru.pda.chemistry.entities.attributeValue;

import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.AttributeType;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * User: 1
 * Date: 05.06.2010
 * Time: 12:34:53
 */
public class DoubleAttributeValue extends AttributeValue
{
	public DoubleAttributeValue() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		this.format = new DecimalFormat("0.000", symbols);
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = (Double) value;
	}
	public String getSqlAttributeValue() {
		if (value == null)
			return "null";

		return format.format(value);
	}
	public String getHtmlAttributeValue() {
		if (value == null)
			return "-";

		return format.format(value);
	}

	public void setAttribute(Attribute attribute) {
		if (attribute.getType() != AttributeType.DOUBLE)
			throw new IllegalArgumentException("Attribute type must be double");

		this.attribute = attribute;
	}

	private Double value;
	private DecimalFormat format;
}