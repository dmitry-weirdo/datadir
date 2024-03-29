package ru.datadir.model.attribute;

import ru.datadir.model.entity.Entity;
import ru.datadir.model.validation.Validator;
import su.opencode.kefir.srv.json.Json;
import su.opencode.kefir.srv.json.JsonObject;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 16.05.13
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Атрибут сущности.
 */
public class Attribute extends JsonObject
{
	public Attribute() {
	}
	public Attribute(String name, AttributeType type) {
		this.type = type;
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	@Json(exclude = true)
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public AttributeType getType() {
		return type;
	}
	public void setType(AttributeType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Boolean getAllowBlank() {
		return allowBlank;
	}
	public void setAllowBlank(Boolean allowBlank) {
		this.allowBlank = allowBlank;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient // todo: use non-transient validators
	public List<Validator> getValidators() {
		return validators;
	}
	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}

	/**
	 * Идентификатор.
	 */
	private Long id;

	/**
	 * Код сущности, к которой относится атрибут.
	 */
	private Long entityId;

	/**
	 * Сущность, к которой относится атрибут.
	 */
	private Entity entity;

	/**
	 * Порядок поля среди полей сущности.
	 */
	private Integer displayOrder;

	/**
	 * Тип атрибута.
	 */
	private AttributeType type;

	/**
	 * Имя атрибута. Валидный идентификатор для поля базы данных.
	 */
	private String name;

	/**
	 * Лейбл для поля атрибута.
	 */
	private String label;

	/**
	 * <code>true</code> &mdash; если поле может быть пустым (<code>null</code>)
	 * <br/>
	 * <code>false</code> &mdash; если поле не может быть пустым
	 */
	private Boolean allowBlank;

	/**
	 * Максимальная длина поля.
	 * Если не указана, то поле не имеет ограничения по длине.
	 * // todo: возможно, это стоит перенести в тип атрибута или в валидатор
	 */
	private Integer maxLength;

	/**
	 * Комментарий к полю.
	 */
	private String comment;

	/**
	 * Валидаторы, применимые к значению атрибута.
	 */
	private List<Validator> validators;
}