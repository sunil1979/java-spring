var emArray = new Array();
var gridHeight=75;
var gridWidth=50;

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
	/*
	for (var i = 0; i < emArray.length; i++ ){
		var emNode = emArray[i];
		var emNodeStr = serializeXmlNode(emNode);
		console.log($(emNodeStr).find('id:first').text());
	}
	*/
	/*
	for (var i = 0; i < emArray.length; i++ ){
		var emNode = emArray[i];
		var childNodes = emNode.childNodes;
		for(var j = 0; j < childNodes.length ; j++){
			if(childNodes[j].nodeName == "id"){
				console.log(childNodes[j].nodeName);
				console.log(childNodes[j].firstChild.nodeValue);
			}
		}
	}
	*/
	//load grouptemplate.svg
	var w=window,d=document,e=d.documentElement,g=d.getElementsByTagName('body')[0];x=w.innerWidth||e.clientWidth||g.clientWidth;x=x-gridWidth;y=w.innerHeight||e.clientHeight||g.clientHeight;y=y-gridHeight;
	$('#mainDiv').width(x);$('#mainDiv').height(y);
	// load GroupTemplateWorking.svg as main template for grid and other controls. Call fnAfterTemplateLoaded to init SVGPan
	$('#mainDiv').svg('destroy');
	$('#mainDiv').svg({loadURL: 'GroupTemplateWorking.svg', addTo: false, onLoad: fnAfterTemplateLoaded});

}


function fnAfterTemplateLoaded(svg, error){
	$(this, $('#mainDiv').svg('get')).attr("xmlns:attrib","http://www.carto.net/attrib");
	var w=window,d=document,e=d.documentElement,g=d.getElementsByTagName('body')[0];x=w.innerWidth||e.clientWidth||g.clientWidth;x = x-gridWidth;y=w.innerHeight||e.clientHeight||g.clientHeight;y = y-gridHeight;
	$('#svgCanvas').width(x);$('#svgCanvas').height(y);
	var viewBoxStr = "0 0 "+x+" "+y;
	$('#svgCanvas').attr('viewBox', viewBoxStr);
	var svgWrapper = $('#mainDiv').svg('get');
	var viewport = svgWrapper.getElementById('viewport');  
	var container = svgWrapper.getElementById('Container');  
    if(container != null) svgWrapper.remove(container);  
    container = svgWrapper.group(viewport,"Container",{changeSize: true});

	for (var i = 0; i < emArray.length; i++ ){
		var idNodeXPathResult = document.evaluate('./id/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		//customize,elementGroup,elementGroupTag,positionX,positionY,positionZ,prime,quantity,rotate,scale,tag,yOrder,yscale,zOrder
		var customizeXPathResult = document.evaluate('./customize/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var elementGroupXPathResult = document.evaluate('./elementGroup/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var elementGroupTagXPathResult = document.evaluate('./elementGroupTag/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var positionXXPathResult = document.evaluate('./positionX/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var positionYXPathResult = document.evaluate('./positionY/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var positionZXPathResult = document.evaluate('./positionZ/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var primeXPathResult = document.evaluate('./prime/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var quantityXPathResult = document.evaluate('./quantity/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var rotateXPathResult = document.evaluate('./rotate/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var scaleXPathResult = document.evaluate('./scale/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var tagXPathResult = document.evaluate('./tag/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var yOrderXPathResult = document.evaluate('./yOrder/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var yscaleXPathResult = document.evaluate('./yscale/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var zOrderXPathResult = document.evaluate('./zOrder/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		var elementIdNodeXPathResult = document.evaluate('./element/id/text()',emArray[i],null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
		/*
		console.log("*****************************************************************************"+i);
		if(idNodeXPathResult)
			console.log("ElementManifest Id :"+idNodeXPathResult.nodeValue);
		if(customizeXPathResult)
			console.log("ElementManifest customize :"+customizeXPathResult.nodeValue);
		if(elementGroupXPathResult)
			console.log("ElementManifest elementGroup :"+elementGroupXPathResult.nodeValue);
		if(elementGroupTagXPathResult)
			console.log("ElementManifest elementGroupTag :"+elementGroupTagXPathResult.nodeValue);
		if(positionXXPathResult)
			console.log("ElementManifest positionX :"+positionXXPathResult.nodeValue);
		if(positionYXPathResult)
			console.log("ElementManifest positionY :"+positionYXPathResult.nodeValue);
		if(positionZXPathResult)
			console.log("ElementManifest positionZ :"+positionZXPathResult.nodeValue);
		if(primeXPathResult)
			console.log("ElementManifest prime :"+primeXPathResult.nodeValue);
		if(quantityXPathResult)
			console.log("ElementManifest quantity :"+quantityXPathResult.nodeValue);
		if(rotateXPathResult)
			console.log("ElementManifest rotate :"+rotateXPathResult.nodeValue);
		if(scaleXPathResult)
			console.log("ElementManifest scale :"+scaleXPathResult.nodeValue);
		if(tagXPathResult)
			console.log("ElementManifest tag :"+tagXPathResult.nodeValue);
		if(yOrderXPathResult)
			console.log("ElementManifest yOrder :"+yOrderXPathResult.nodeValue);
		if(yscaleXPathResult)
			console.log("ElementManifest yscale :"+yscaleXPathResult.nodeValue);
		if(zOrderXPathResult)
			console.log("ElementManifest zOrder :"+zOrderXPathResult.nodeValue);
		if(elementIdNodeXPathResult)
			console.log("Child Element Id :"+elementIdNodeXPathResult.nodeValue);
		console.log("*****************************************************************************");
		*/
		//update container
		var transformStr = "rotate("+rotateXPathResult.nodeValue+" "+positionXXPathResult.nodeValue+","+positionYXPathResult.nodeValue+") translate("+positionXXPathResult.nodeValue+","+positionYXPathResult.nodeValue+") scale("+ scaleXPathResult.nodeValue+ ")";
		var elementGroupId = idNodeXPathResult.nodeValue;
		svgWrapper.group(container,elementGroupId,{transform: transformStr});
		if(emArray[i].parentNode.parentNode && emArray[i].parentNode.parentNode.nodeName == "M"){
			console.log("parent em node found :"+i);
			console.log("Node name :"+emArray[i].parentNode.parentNode.nodeName);
			var parentEM = emArray[i].parentNode.parentNode;
			var parentEMID = document.evaluate('./id/text()',parentEM,null,XPathResult.ORDERED_NODE_ITERATOR_TYPE, null).iterateNext();
			container=svgWrapper.getElementById(parentEMID.nodeValue);
		}
		var view = 'svg/1/'+elementIdNodeXPathResult.nodeValue;
		var elementGroup = svg.getElementById(elementGroupId);
		var elementSVGId = elementGroupId+'_E';
		var element = svgWrapper.group(elementGroup, elementSVGId);
		element = $('#'+elementSVGId, svgWrapper.root());
		element.svg({loadURL: view, svgTransform: transformStr, addTo: false, 
			changeSize: false, onLoad: loadDone});
	}
}


function loadDone(svg, error) { 
	//console.log(this);
}

function recurseElements(elements) {
	//console.log(elements);
	var elementManifestXPathResult = document.evaluate('./M', elements, null,
			XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
	var em = elementManifestXPathResult.iterateNext();
	while (em) {
		//console.log(em);
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
