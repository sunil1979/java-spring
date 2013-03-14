$.Controller("Ebuild.Cnt", {
	init : function() {
		
	},

	// create event listeners for UI events
	".getHomeUnitRevision click" : function() {
		console.log("Get HomeUnitRevision Clicked");
		this.options.model.getHomeUnitRevision(this.options.model);
	},
	
	".getUserInfoEvent click" : function() {
		console.log("Get User Info Button Clicked");
		this.options.model.getUserInfo(this.options.model);
	},

	".getHomeUnitEvent click" : function(){
		console.log("Get Home Unit Button Clicked");
		this.options.model.getHomeUnits(this.options.model);
	},
	
	".getProjectEvent click" : function(){
		console.log("Get Project Button Clicked");
		this.options.model.getProject(this.options.model);
	},
	
	".getVersionEvent click" : function(){
		console.log("Get Version Button Clicked");
		this.options.model.getVersion(this.options.model);
	},
	
	".getRevisionsEvent click" : function(){
		console.log("Get Revisions Button Clicked");
		this.options.model.getRevisions(this.options.model);
	},
	
	//All event listeners on model
	"{model} latestRevisionXML" : function(){
		console.log("latestRevisionXML changed :"+this.options.model.latestRevisionXML);
		parseRevisionXML(this.options.model.latestRevisionXML);
	},
	
	"{model} userInfoXML" : function(){
		console.log("userInfoXML changed :"+this.options.model.userInfoXML);
		var userInfoXMLDoc = serializeXmlNode(this.options.model.userInfoXML);
		var userId = $(userInfoXMLDoc).find('id:first').text();
		this.options.model.attr('userId',userId);
		$("#mainDiv").append('./js/ebuild/view/userinfo_view.ejs',{userInfo: userInfoXMLDoc});
	},
	
	"{model} homeUnitXML" : function(){
		console.log("homeUnitXML changed :"+this.options.model.homeUnitXML);
		var homeUnitXMLDoc = serializeXmlNode(this.options.model.homeUnitXML);
		var projectId = $(homeUnitXMLDoc).find('projectid:first').text();
		var homeUnitId = $(homeUnitXMLDoc).find('id:first').text();
		this.options.model.attr('projectId',projectId);
		this.options.model.attr('homeUnitId',homeUnitId);
		$("#mainDiv").append('./js/ebuild/view/homeunitinfo_view.ejs',{homeUnitInfo: homeUnitXMLDoc});
	},
	
	"{model} projectXML" : function(){
		console.log("projectXML changed :"+this.options.model.projectXML);
		projectXMLDoc = serializeXmlNode(this.options.model.projectXML);
		$("#mainDiv").append('./js/ebuild/view/projectinfo_view.ejs',{projectInfo: projectXMLDoc});
	},
	
	"{model} versionXML" : function(){
		console.log("versionXML changed :"+this.options.model.versionXML);
		var versionXMLDoc = serializeXmlNode(this.options.model.versionXML);
		var versionId = $(versionXMLDoc).find('id:first').text();
		this.options.model.attr('versionId',versionId);
		$("#mainDiv").append('./js/ebuild/view/versioninfo_view.ejs',{versionInfo: versionXMLDoc});
	},
	
	"{model} revisionsXML" : function(){
		console.log("revisionsXML changed :"+this.options.model.revisionsXML);
		var revisionsXMLDoc = serializeXmlNode(this.options.model.revisionsXML);
		var latestRevisionId = $(revisionsXMLDoc).find('id:first').text();
		this.options.model.attr('latestRevisionId',latestRevisionId);
		$("#mainDiv").append('./js/ebuild/view/revisionsinfo_view.ejs',{revisionsInfo: revisionsXMLDoc});
		this.getRevision();
	},
	
	"{model} revisionXML" : function(){
		console.log("revisionXML changed :"+this.options.model.revisionXML);
		var revisionXMLDoc = serializeXmlNode(this.options.model.revisionXML);
		$("#mainDiv").append('./js/ebuild/view/revisioninfo_view.ejs',{revisionInfo: revisionXMLDoc});
	},
		
	//Call Back Events
	getRevision : function(){
		console.log("CONTROLLER - getRevision Called");
		this.options.model.getRevision(this.options.model);
	}
});

$(function() {
	$("#inputDiv").ebuild_cnt({model: new ebuild()});
});
