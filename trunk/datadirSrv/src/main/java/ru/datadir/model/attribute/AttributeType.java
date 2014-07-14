package ru.datadir.model.attribute;

import su.opencode.kefir.srv.json.JsonObject;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 16.05.13
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Тип атрибута сущности. Хранится в базе данных.
 * Должен быть замаплен на тип поля базы данных.
 */
public class AttributeType extends JsonObject
{
	public AttributeType() {
	}
	public AttributeType(AttributeTypeMnemonic mnemonic) {
		this.mnemonic = mnemonic;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AttributeTypeMnemonic getMnemonic() {
		return mnemonic;
	}
	public void setMnemonic(AttributeTypeMnemonic mnemonic) {
		this.mnemonic = mnemonic;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Идентификатор.
	 */
	private Long id;

	/**
	 * Мнемоника.
	 */
	private AttributeTypeMnemonic mnemonic;

	/**
	 * Java-тип. Например, String.
	 */
	private String javaType;

	/**
	 * Тип в базе данных. Например, varchar.
	 */
	private String databaseType;

	/**
	 * Наименование типа (например, &laquo;целое число&raquo;.
	 */
	private String title;


	// todo: дополнительные параметры.
}