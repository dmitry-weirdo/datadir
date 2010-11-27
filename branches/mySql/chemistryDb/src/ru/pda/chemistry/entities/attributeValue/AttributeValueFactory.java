package ru.pda.chemistry.entities.attributeValue;

import ru.pda.chemistry.entities.Attribute;
import static ru.pda.chemistry.util.StringUtils.getConcatenation;
import ru.pda.chemistry.service.ConstructorService;
import ru.pda.chemistry.service.ServiceFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: 1
 * Date: 06.06.2010
 * Time: 14:05:52
 */
public class AttributeValueFactory
{
	public static AttributeValue createAttributeValue(Attribute attribute) {
		switch (attribute.getType())
		{
			case BOOLEAN: return new BooleanAttributeValue();
			case ENUM: return new EnumAttributeValue();
			case DOUBLE: return new DoubleAttributeValue();
			case INTEGER: return new IntegerAttributeValue();
			case STRING: return new StringAttributeValue();

			default:
				throw new IllegalArgumentException(getConcatenation("Incorrect attribute type: ", attribute.getType().toString()));
		}
	}

	public static void setAttributeValue(AttributeValue value, ResultSet resultSet, int columnIndex) throws SQLException {
		switch (value.getAttribute().getType())
		{
			case BOOLEAN: value.setValue(resultSet.getBoolean(columnIndex)); break;
			case DOUBLE: value.setValue(resultSet.getDouble(columnIndex)); break;
			case INTEGER: value.setValue(resultSet.getLong(columnIndex)); break;
			case STRING: value.setValue(resultSet.getString(columnIndex)); break;

			// special case - needs to set also string value of
			case ENUM:
				Integer enumValueId = resultSet.getInt(columnIndex);
				value.setValue(enumValueId);

				if (!resultSet.wasNull())
					((EnumAttributeValue) value).setValueStr(service.getEnumValue(enumValueId).getName());

				break;
		}

		if (resultSet.wasNull())
			value.setValue(null);
	}

	private static ConstructorService service = ServiceFactory.getConstructorService();
}
