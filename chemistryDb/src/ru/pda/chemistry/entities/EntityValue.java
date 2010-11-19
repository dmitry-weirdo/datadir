package ru.pda.chemistry.entities;

import ru.pda.chemistry.entities.attributeValue.AttributeValue;

import java.util.Map;
import java.util.HashMap;

/**
 * User: 1
 * Date: 06.06.2010
 * Time: 13:47:39
 */
public class EntityValue
{
	public EntityValue() {
		values = new HashMap<String, AttributeValue>();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public AttributeValue getAttributeValue(String attributeName) {
		return values.get(attributeName);
	}
	public void setAttributeValue(String attributeName, AttributeValue value) {
		values.put(attributeName, value);
	}

	private Integer id;
	private String source;
	private Map<String, AttributeValue> values;
}