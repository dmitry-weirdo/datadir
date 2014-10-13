/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.datadir.section;

import ru.datadir.model.section.Section;
import ru.datadir.model.section.SectionService;
import su.opencode.kefir.web.Action;
import su.opencode.kefir.web.InitiableAction;
import su.opencode.kefir.web.JsonServlet;

import java.util.List;

/**
 * Example usages:
 * <ul>
 *   <li><a href="http://localhost:8080/datadir/sectionsList">http://localhost:8080/datadir/sectionsList</a></li>
 *   <li><a href="http://localhost:8080/datadir/sectionsList?parentSectionId=1">http://localhost:8080/datadir/sectionsList?parentSectionId=1</a></li>
 * </ul>
 */
public class SectionsListServlet extends JsonServlet
{
	protected Action getAction() {
		return new InitiableAction()
		{
			public void doAction() throws Exception {
				Long parentSectionId = getLongParam("parentSectionId");

				SectionService service = getService(SectionService.class);

				List<Section> sections = service.getSections(parentSectionId);
				writeSuccess(sections);
			}
		};
	}
}