var emArray = new Array();
var gridHeight = 75;
var gridWidth = 50;
var w = window;
var d = document;
var e = d.documentElement;
var g = d.getElementsByTagName('body')[0];


function serializeXmlNode(xmlNode) {
	try {
		// Gecko-based browsers, Safari, Opera.
		return (new XMLSerializer()).serializeToString(xmlNode);
	} catch (e) {
		try {
			// Internet Explorer.
			return xmlNode.xml;
		} catch (e) {// Strange Browser ??
			alert('Xmlserializer not supported');
		}
	}
	return false;
}

function parseRevisionXML(xmlNode) {
	console.log("parseRevisionXML called");
	console.log(xmlNode);
	var xmlDoc = loadXMLString(serializeXmlNode(xmlNode));
	if (document.evaluate) {
		var xpath = '/ebuildleapResultObject/homeUnitRevision/homeUnitVersion/homeUnit/product/element';
		var topElementsXPathResult = document.evaluate(xpath, xmlDoc, null, 5,
				null);
		var topElements = topElementsXPathResult.iterateNext();
		while (topElements) {
			recurseElements(topElements);
			topElements = topElementsXPathResult.iterateNext();
		}
		console.log("EM Array Length :" + emArray.length);
	} else {
		console.log("Browser does not support evaluate method");
	}

	x = w.innerWidth || e.clientWidth || g.clientWidth;
	x = x - gridWidth;
	y = w.innerHeight || e.clientHeight || g.clientHeight;
	y = y - gridHeight;
	$('#mainDiv').width(x);
	$('#mainDiv').height(y);
	// load GroupTemplateWorking.svg as main template for grid and other
	// controls. Call fnAfterTemplateLoaded to init SVGPan
	$('#mainDiv').svg('destroy');
	$('#mainDiv').svg({
		loadURL : 'GroupTemplateWorking.svg',
		addTo : false,
		onLoad : fnAfterTemplateLoaded
	});
	

}

function fnAfterTemplateLoaded(svg, error) {
	console.log("fnAfterTemplateLoaded called");

	$(this, $('#mainDiv').svg('get')).attr("xmlns:attrib",
			"http://www.carto.net/attrib");
	x = w.innerWidth || e.clientWidth || g.clientWidth;
	x = x - gridWidth;
	y = w.innerHeight || e.clientHeight || g.clientHeight;
	y = y - gridHeight;
	$('#svgCanvas').width(x);
	$('#svgCanvas').height(y);
	//var viewBoxStr = "0 0 " + x + " " + y;
	var viewBoxStr = "0 0 45947 11339";
	$('#svgCanvas').attr('viewBox', viewBoxStr);

	
	
	var svgWrapper = $('#mainDiv').svg('get');
	var viewPortWrapper = svgWrapper.getElementById('viewport');
	container = svg.group(viewPortWrapper, "Container", {
		changeSize : true
	});
	container = svg.group(container,"mainElement",{
		transform : "translate(0,0) rotate(0) scale(1,1)"
	});
	for ( var i = 0; i < emArray.length; i++) {
		var idNodeXPathResult = document.evaluate('./id/text()', emArray[i],
				null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null)
				.iterateNext();
		// customize,elementGroup,elementGroupTag,positionX,positionY,positionZ,prime,quantity,rotate,scale,tag,yOrder,yscale,zOrder
		var positionXXPathResult = document.evaluate('./positionX/text()',
				emArray[i], null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null)
				.iterateNext();
		var positionYXPathResult = document.evaluate('./positionY/text()',
				emArray[i], null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null)
				.iterateNext();
		var rotateXPathResult = document.evaluate('./rotate/text()',
				emArray[i], null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null)
				.iterateNext();
		var scaleXPathResult = document.evaluate('./scale/text()', emArray[i],
				null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null)
				.iterateNext();
		var elementIdNodeXPathResult = document.evaluate('./element/id/text()',
				emArray[i], null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null)
				.iterateNext();
		// update container
		transformStr = "rotate(" + rotateXPathResult.nodeValue + " "
		+ positionXXPathResult.nodeValue + ","
		+ positionYXPathResult.nodeValue + ") translate("
		+ positionXXPathResult.nodeValue + ","
		+ positionYXPathResult.nodeValue + ") scale("
		+ scaleXPathResult.nodeValue + ")";

		var elementManifestGroupId = idNodeXPathResult.nodeValue;
		if (emArray[i].parentNode.parentNode
				&& emArray[i].parentNode.parentNode.nodeName == "M") {
			var parentEM = emArray[i].parentNode.parentNode;
			var parentEMID = document.evaluate('./id/text()', parentEM, null,
					XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
			container = svgWrapper.getElementById(parentEMID.nodeValue);
		} else {
			container = svgWrapper.getElementById("mainElement");
		}

		var emGroup = svg.group(container, elementManifestGroupId, {
			transform : transformStr
		});
		var view = 'svg/1/' + elementIdNodeXPathResult.nodeValue;
		var elementSVGId = elementManifestGroupId + '_E';
		svg.group(emGroup, elementSVGId);
		$('#' + elementSVGId).svg({
			loadURL : view,
			svgTransform : transformStr,
			addTo : false,
			changeSize : false,
			onLoad: loadDone
		});
	}

}

function loadDone(svg, error) {
	// console.log(this);
}

function recurseElements(elements) {
	// console.log(elements);
	var elementManifestXPathResult = document.evaluate('./M', elements, null,
			XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
	var em = elementManifestXPathResult.iterateNext();
	while (em) {
		// console.log(em);
		emArray.push(em);
		var elementsXPathResult = document.evaluate('./element', em, null,
				XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		var innerElements = elementsXPathResult.iterateNext();
		while (innerElements) {
			recurseElements(innerElements);
			innerElements = elementsXPathResult.iterateNext();
		}
		em = elementManifestXPathResult.iterateNext();
	}
}

function loadXMLString(txt) {
	// console.log(txt);
	if (window.DOMParser) {
		parser = new DOMParser();
		xmlDoc = parser.parseFromString(txt, "text/xml");
	} else // Internet Explorer
	{
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;
		xmlDoc.loadXML(txt);
	}
	return xmlDoc;
}
