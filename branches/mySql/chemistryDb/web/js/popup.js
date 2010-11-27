/*
	Popup forms support functions.

	Copyright 2008 SEC "Knowledge Genesis", Ltd.
	http://www.kg.ru, http://www.knowledgegenesis.com
	$Author: pda$
*/

function createClass(ancestor) {
	var clazz = function() {
		this.initialize.apply(this, arguments);
	}

	if (ancestor)
		for (var property in ancestor)
			clazz.prototype[property] = ancestor[property];

	return clazz;
}

var ShadeWindow = createClass();
ShadeWindow.prototype.initialize = function () {
	this.shadeDiv = document.getElementById('shade');
}
ShadeWindow.prototype.createShadeDiv = function() {
	this.shadeDiv = document.createElement('div');
	this.shadeDiv.id = 'shade';
	this.shadeDiv.style.top = '0';
	this.shadeDiv.style.left = '0';
	this.shadeDiv.style.position = 'absolute';
	document.body.appendChild(this.shadeDiv);
}
ShadeWindow.prototype.open = function() {
	this.hideSelects();
	this.show();
}
ShadeWindow.prototype.close = function() {
	this.hide();
	this.showSelects();
}
ShadeWindow.prototype.show = function() {
	if (!this.shadeDiv)
		this.createShadeDiv();
	this.correct();
	this.shadeDiv.style.display = 'block';
}
ShadeWindow.prototype.hide = function() {
	this.shadeDiv.style.display = 'none';
}
ShadeWindow.prototype.correct = function() {
	this.shadeDiv.style.width = document.documentElement.scrollWidth + 'px';
	this.shadeDiv.style.height = document.documentElement.scrollHeight + 'px';
}
ShadeWindow.prototype.hideSelects = function() {}
ShadeWindow.prototype.showSelects = function() {}

var PopupWindow = createClass();
PopupWindow.windows = new Array(); // stack of all windows
PopupWindow.stack = new Array(); // stack of shown windows
PopupWindow.shade = new ShadeWindow();

// common functions for all browsers
PopupWindow.prototype.initialize = function(blockId) {
	PopupWindow.windows.push(this);
	if (blockId)
	{
		this.block = document.getElementById(blockId);
		if (this.block)
			this.block.style.position = 'absolute';
	}
}
PopupWindow.prototype.open = function() {
	if (PopupWindow.stack.length)
		PopupWindow.stack[PopupWindow.stack.length - 1].hide();
	else
		PopupWindow.shade.open();

	PopupWindow.stack.push(this);

	window.scrollTo(0, 0);

	this.show();
}
PopupWindow.prototype.close = function() {
	this.hide();

	if (PopupWindow.stack.pop() != this)
		throw new Error('This object is not on the top of window stack');

	if (PopupWindow.stack.length)
		PopupWindow.stack[PopupWindow.stack.length - 1].show(); // display previous window
	else
		PopupWindow.shade.close(); // close shade
}
PopupWindow.prototype.show = function() {
	this.block.style.display = 'block';
	this.correct();
}
PopupWindow.prototype.hide = function() {
	this.block.style.display = 'none';
}
PopupWindow.prototype.correct = function() {
	PopupWindow.shade.hide();

	this.block.style.left = '0';
	this.block.style.top = '0';

	var left = Math.floor((document.documentElement.clientWidth - this.block.offsetWidth) / 2);
	var top = Math.floor((document.documentElement.clientHeight - this.block.offsetHeight) / 2);
	if (left > 0) this.block.style.left = left + 'px';
	if (top > 0) this.block.style.top = top + 'px';

	PopupWindow.shade.show();
}
PopupWindow.prototype.fillForm = function(values) {
	// todo: think about not directly form subelements as fields
	var form = PopupWindow.findForm(this.block);
	for (var i = 0; i < form.elements.length; i++)
	{
		var e = form.elements[i];
		var v = values[e.name];
		if (v)
		{
			switch (e.type.toLowerCase())
			{
				case 'text':
				case 'hidden':
				case 'textarea':
				case 'password':
					e.value = v;
					break;
				case 'select-one':
					for (var loop = 0; loop < e.options.length; loop++)
					{
						if (e.options[loop].value == v)
						{
							e.selectedIndex = loop;
							break;
						}
					}
					break;
				case 'radio':
				case 'checkbox':
					e.checked = (e.value == v);
					break;
			}
		}
	}
};
PopupWindow.prototype.focusElement = function(focusElemName) {
	var form = PopupWindow.findForm(this.block);
	if (focusElemName && form.elements[focusElemName] && form.elements[focusElemName].type.toLowerCase() != 'hidden')
	{ // focus desired element
		form.elements[focusElemName].focus();
	}
	else
	{ // focus first visible element
		for (var i = 0; i < form.elements.length; i++)
		{
			var elem = form.elements[i];
			if (elem.type.toLowerCase() != 'hidden')
			{
				elem.focus();
				break;
			}
		}
	}
}
PopupWindow.bindFunction = function(windowIndex, windowFunction) {
	return function() {
		return windowFunction.apply(PopupWindow.windows[windowIndex], arguments);
	}
}
PopupWindow.findWindow = function(blockId) {
	for (var i = 0; i < PopupWindow.windows.length; i++)
		if (PopupWindow.windows[i].block && PopupWindow.windows[i].block.id == blockId)
			return PopupWindow.windows[i];

	return null;
}
PopupWindow.findForm = function(block) {
	for (var node = block.firstChild; node; node = node.nextSibling)
		if (node.nodeType == 1 && node.nodeName.toLowerCase() == 'form')
			return node;

	return null;
}

// overloaded functions for ie
if (/msie/i.test(navigator.userAgent) && !/opera/i.test(navigator.userAgent)) {
	ShadeWindow.prototype.hideSelects = function(node) {
		if (!node)
		{
			this.hiddenSelects = new Array();
			this.hideSelects(document.documentElement);
		}
		else if (node.currentStyle.display != 'none' && node.currentStyle.visibility != 'hidden')
		{
			if (node.nodeName.toLowerCase() == 'select')
			{
				node.style.visibility = 'hidden';
				this.hiddenSelects.push(node);
			}
			else
			{
				for (var child = node.firstChild; child; child = child.nextSibling)
					if (child.nodeType == 1)
						this.hideSelects(child);
			}
		}
	}
	ShadeWindow.prototype.showSelects = function() {
		while (this.hiddenSelects.length)
			this.hiddenSelects.pop().style.visibility = 'visible';
	}
	ShadeWindow.prototype.correct = function(block) {
		var right = block? block.offsetLeft + block.offsetWidth: 0;
		var bottom = block? block.offsetTop + block.offsetHeight: 0;
		if (right < document.body.offsetWidth) right = document.body.offsetWidth;
		if (bottom < document.body.offsetHeight) bottom = document.body.offsetHeight;
		if (right < document.documentElement.clientWidth) right = document.documentElement.clientWidth;
		if (bottom < document.documentElement.clientHeight) bottom = document.documentElement.clientHeight;
		this.shadeDiv.style.width = right + 'px';
		this.shadeDiv.style.height = bottom + 'px';
	}
	PopupWindow.prototype.correct = function() {
		var left = Math.floor((document.documentElement.clientWidth - this.block.offsetWidth) / 2); if (left < 0) left = 0;
		var top = Math.floor((document.documentElement.clientHeight - this.block.offsetHeight) / 2); if (top < 0) top = 0;
		if (this.block.offsetLeft != left) this.block.style.left = left + 'px';
		if (this.block.offsetTop != top) this.block.style.top = top + 'px';
		PopupWindow.shade.correct(this.block);
	}
}

// bind window resize event handlers
function popupWindowOnResize() {
	if (PopupWindow.stack.length)
		PopupWindow.stack[PopupWindow.stack.length - 1].correct();
}
window.onresize = popupWindowOnResize;

// functions of the outer interface
function showForm(blockId) {
	var popupWindow = PopupWindow.findWindow(blockId);
	if (!popupWindow)
		popupWindow = new PopupWindow(blockId);

	popupWindow.open();
}
function fillForm(blockId, values) {
	var popupWindow = PopupWindow.findWindow(blockId);
	if (!popupWindow)
		popupWindow = new PopupWindow(blockId);

	popupWindow.fillForm(values);
}
function hideForm(blockId) {
	var popupWindow = PopupWindow.findWindow(blockId);
	if (popupWindow)
		popupWindow.close();
}
function focusElement(blockId, elemName) {
	var popupWindow = PopupWindow.findWindow(blockId);
	if (popupWindow)
		popupWindow.focusElement(elemName);
}