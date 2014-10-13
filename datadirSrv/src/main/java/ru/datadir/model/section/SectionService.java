/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.datadir.model.section;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SectionService
{
	List<Section> getSections(Long parentSectionId);

	List<Section> getSections(Long parentSectionId, String name);

	Long createSection(Section section);

	void updateSection(Section section);

	void deleteSection(Long sectionId);
}