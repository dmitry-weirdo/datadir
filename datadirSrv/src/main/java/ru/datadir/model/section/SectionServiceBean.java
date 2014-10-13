/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.datadir.model.section;

import org.apache.log4j.Logger;
import su.opencode.kefir.util.SqlUtils;
import su.opencode.kefir.util.StringUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

@Stateless
public class SectionServiceBean implements SectionService
{
	@Override
	@SuppressWarnings("unchecked")
	public List<Section> getSections(Long parentSectionId) {
		Query query;
		if (parentSectionId == null)
		{
			query = em.createQuery("select s from Section s where (s.parent.id is null) order by s.name");
		}
		else
		{
			query = em.createQuery("select s from Section s where (s.parent.id = :parentSectionId) order by s.name")
				.setParameter("parentSectionId", parentSectionId);
		}

		return (List<Section>) query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Section> getSections(Long parentSectionId, String name) {
		Query query;
		if (parentSectionId == null)
		{
			query = em.createQuery("select s from Section s where (s.parent.id is null) and (s.name like :name) order by s.name")
				.setParameter("name", SqlUtils.getSearchParamFirebird(name));
		}
		else
		{
			query = em.createQuery("select s from Section s where (s.parent.id = :parentSectionId) and (s.name like :name) order by s.name")
				.setParameter("parentSectionId", parentSectionId)
				.setParameter("name", SqlUtils.getSearchParamFirebird(name));
		}

		return (List<Section>) query.getResultList();
	}

	@Override
	public Long createSection(Section section) {
		if ( section == null)
			throw new IllegalArgumentException("Section cannot be null");

		Section parent = section.getParent();
		if (parent != null)
		{
			Section parentSection = em.find(Section.class, parent.getId());
			if (parentSection == null)
				throw new IllegalArgumentException( concat(sb, "Parent Section with id = ", parent.getId(), " does not exist") );
		}

		if ( StringUtils.empty(section.getName()) )
			throw new IllegalArgumentException("Section name cannot be null or empty");

		em.persist(section);
		return section.getId();
	}

	@Override
	public void updateSection(Section section) {
		Section existingSection = em.find(Section.class, section.getId());
		if ( existingSection == null )
			throw new IllegalArgumentException( concat(sb, "Section with id = ", section.getId(), " does not exist") );

		if ( StringUtils.empty(section.getName()) )
			throw new IllegalArgumentException("Section name cannot be null or empty");

		existingSection.setName( section.getName() );
		existingSection.setComment(section.getComment());
	}

	@Override
	public void deleteSection(Long sectionId) {
		Section section = em.find(Section.class, sectionId);
		if (section == null)
			return;

		// todo: validate child sections non-existence
		// todo: validate child entities non-existence
		em.remove(section);
	}

	@PersistenceContext
	private EntityManager em;

	private StringBuffer sb = new StringBuffer();

	private static final Logger logger = Logger.getLogger(SectionServiceBean.class);
}