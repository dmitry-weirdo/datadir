/**
 * No usage of any js-library framework.
 * // todo: namespace
 */
var NBSP_CODE = '\u00a0';

function createEntityTable(config) {
	// todo: validate config

	var i, j;
	var attribute;
	var value;
	var dh = DomHelper;

	// todo: ids of all elements
	// todo: metadata fill validation
	var header = dh.createEl(dh.H2_TAG, null, null, config.metadata.label);

	var table = dh.createEl(dh.TABLE_TAG);
	table.setAttribute(dh.BORDER_ATTRIBUTE, 1);

	var tableBody = dh.createEl(dh.TBODY_TAG);


	var headerRow = dh.createEl(dh.TR_TAG);
	for (i = 0; i < config.metadata.attributes.length; i++)
	{
		attribute = config.metadata.attributes[i];
		var attributeHeaderCell = dh.createEl(dh.TH_TAG, null, null, attribute.label);
		headerRow.appendChild(attributeHeaderCell);
	}

	var updateHeader = dh.createEl(dh.TH_TAG, null, null, 'Изменить');
	var deleteHeader = dh.createEl(dh.TH_TAG, null, null, 'Удалить');

	headerRow.appendChild(updateHeader);
	headerRow.appendChild(deleteHeader);

	tableBody.appendChild(headerRow);


	for (i = 0; i < config.data.length; i++)
	{
		var record = config.data[i];

		var recordRowClass = null;
		if ( (i % 2 == 0) && (config.evenTrClass) )
			recordRowClass = config.evenTrClass;
		else if ( (i % 2 == 1) && (config.oddTrClass) )
			recordRowClass = config.oddTrClass;


		var recordRow = dh.createEl(dh.TR_TAG, null, recordRowClass);

		for (j = 0; j < config.metadata.attributes.length; j++)
		{
			attribute = config.metadata.attributes[j];
			value = record[attribute.name];
			if (!value)
				value = NBSP_CODE; // todo: move to NBSP_CODE constant and renderer

			// todo: apply value renderer
			var valueCell = dh.createEl(dh.TD_TAG, null, null, value);
			recordRow.appendChild(valueCell);
		}

		var updateCell = dh.createEl(dh.TD_TAG);
		var updateLink = dh.createEl(dh.A_TAG, null, null, 'Изменить');
		updateLink.setAttribute(dh.HREF_ATTRIBUTE, '/update?entityId=' + metadata.id + '&recordId=' + record.id);
		updateCell.appendChild(updateLink);

		var deleteCell = dh.createEl(dh.TD_TAG);
		var deleteLink = dh.createEl(dh.A_TAG, null, null, 'Удалить');
		deleteLink.setAttribute(dh.HREF_ATTRIBUTE, '/delete?entityId=' + metadata.id + '&recordId=' + record.id);
		deleteCell.appendChild(deleteLink);

		recordRow.appendChild(updateCell);
		recordRow.appendChild(deleteCell);

		tableBody.appendChild(recordRow);
	}

	var createRowClass = null;
	if ( (config.data.length % 2 == 0) && (config.evenTrClass) )
		createRowClass = config.evenTrClass;
	else if ( (config.data.length % 2 == 1) && (config.oddTrClass) )
		createRowClass = config.oddTrClass;

	var createRow = dh.createEl(dh.TR_TAG, null, createRowClass);

	var leftCreateCell = dh.createEl(dh.TD_TAG, null, null, NBSP_CODE);
	leftCreateCell.setAttribute(dh.COL_SPAN_ATTRIBUTE, config.metadata.attributes.length);
	leftCreateCell.colSpan = config.metadata.attributes.length; // for IE // todo: do this only for IE

	var createCell = dh.createEl(dh.TD_TAG);
	var createLink = dh.createEl(dh.A_TAG, null, null, 'Создать');
	createLink.setAttribute(dh.HREF_ATTRIBUTE, '/create?entityId=' + metadata.id);
	createCell.appendChild(createLink);

	var rightCreateCell = dh.createEl(dh.TD_TAG, null, null, NBSP_CODE);

	createRow.appendChild(leftCreateCell);
	createRow.appendChild(createCell);
	createRow.appendChild(rightCreateCell);

	tableBody.appendChild(createRow);

	table.appendChild(tableBody);

	var container = document.getElementById(config.containerId);
	container.appendChild(header);
	container.appendChild(table);
}