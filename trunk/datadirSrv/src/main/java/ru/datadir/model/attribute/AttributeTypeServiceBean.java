package ru.datadir.model.attribute;

import ru.datadir.model.entity.Entity;
import su.opencode.kefir.util.ObjectUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Created with IntelliJ IDEA.
 * User: nosferatum
 * Date: 28.05.13
 * Time: 0:00
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class AttributeTypeServiceBean implements AttributeTypeService
{
	@Override
	@SuppressWarnings("unchecked")
	public List<AttributeType> getAttributeTypes() {
		Query query = em.createQuery("select at from AttributeType at");
		return (List<AttributeType>) query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public AttributeType getAttributeType(AttributeTypeMnemonic mnemonic) {
		if (mnemonic == null)
			return null;

		Query query = em.createQuery("select at from AttributeType at where at.mnemonic = :mnemonic")
			.setParameter("mnemonic", mnemonic);

		List<AttributeType> resultList = query.getResultList();
		if ( ObjectUtils.empty(resultList) )
			return null;

		if (resultList.size() > 1)
			throw new IllegalStateException( concat(sb, "More than one Attribute type for mnemonic \"", mnemonic, "\"") );

		return resultList.get(0);
	}

	@Override
	public Entity getEntity(Long id) {
		if (id == null)
			return null;

		Entity entity = em.find(Entity.class, id);
		if (entity == null)
			return null;

		entity.getAttributes().size();
		return entity;
	}

	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();
}