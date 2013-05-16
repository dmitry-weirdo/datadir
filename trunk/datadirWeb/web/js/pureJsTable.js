/**
 * No usage of any js-library framework.
 * // todo: namespace
 */
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
	tableBody.appendChild(headerRow);


	for (i = 0; i < config.data.length; i++)
	{
		var record = config.data[i];
		var recordRow = dh.createEl(dh.TR_TAG);

		for (j = 0; j < config.metadata.attributes.length; j++)
		{
			attribute = config.metadata.attributes[j];
			value = record[attribute.name];
			if (!value)
				value = '\u00a0'; // todo: move to NBSP_CODE constant and renderer

			// todo: apply value renderer
			var valueCell = dh.createEl(dh.TD_TAG, null, null, value);
			recordRow.appendChild(valueCell);
		}

		tableBody.appendChild(recordRow);
	}

	table.appendChild(tableBody);

	var container = document.getElementById(config.containerId);
	container.appendChild(header);
	container.appendChild(table);
}