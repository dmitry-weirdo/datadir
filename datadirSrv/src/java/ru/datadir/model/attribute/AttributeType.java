package ru.datadir.model.attribute;

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
public class AttributeType
{
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	 * Тип. Например, String.
	 */
	private String type;

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