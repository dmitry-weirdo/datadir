package ru.datadir.model.attribute;

import ru.datadir.model.entity.Entity;

import javax.ejb.Local;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 27.05.13
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface AttributeTypeService
{
	List<AttributeType> getAttributeTypes();

	Entity getEntity(Long id);
}