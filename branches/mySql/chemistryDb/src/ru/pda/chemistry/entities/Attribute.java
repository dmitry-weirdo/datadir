package ru.pda.chemistry.entities;

import java.util.List;

/**
 * User: 1
 * Date: 27.04.2010
 * Time: 1:53:16
 * Атрибут сущности.
 * Атрибут имеет тип и единицу измерения (которая может быть не указана в случае отсутствия размерности).
 * Атрибут может иметь тип "множество", при этом набор значений определяется списком Enum.
 * @see ru.pda.chemistry.entities.AttributeType
 * @see EnumValue
 */
public class Attribute
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AttributeType getType() {
		return type;
	}
	public void setType(AttributeType type) {
		this.type = type;
	}
	public String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public List<EnumValue> getValues() {
		return values;
	}
	public void setValues(List<EnumValue> values) {
		this.values = values;
	}

	// transient
	public boolean isEnum() {
		return type == AttributeType.ENUM;
	}

	// transient
	public String getTypeStr() {
		switch (type)
		{
			case BOOLEAN:	return "Логический";
			case ENUM: return "Множество";
			case INTEGER: return "Целый";
			case DOUBLE: return "Вещественный";
			case STRING: return "Строка";
		}

		return "-";
	}


	public static AttributeType getAttributeType(Integer ordinal) {
		if (ordinal == null)
			return null;

		for (AttributeType type : AttributeType.values())
			if (type.ordinal() == ordinal)
				return type;

		return null;
	}

	private Integer id;
	private String name;
	private AttributeType type;
	private String measureUnit;
	private String columnName;
	
	private Entity entity;
	private List<EnumValue> values; // if not isEnum, must be null
}