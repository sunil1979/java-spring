$.Model('ebuild', {

	getHomeUnitRevision : function(passedModel){
		var revisionId = $('#revisionId').val();
		console.log("getHomeUnitRevision called :"+revisionId);
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/eBuildSpringJPAMongo/rest/customizationapi/getHomeUnitRevision",
			data : "homeUnitRevisionId="+revisionId,
			success : function(data, textStatus, oHTTP) {
				passedModel.attr('latestRevisionXML', oHTTP.responseXML);
			},
			error : this.getHomeUnitRevisionErrorHandler
		});		
	},
	
	getHomeUnitRevisionErrorHandler : function() {
		console.log("ERROR RETRIEVING LATEST HOME UNIT REVISION");
	},
	
	getUserInfo : function(passedModel) {
		console.log("getUserInfo called");
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/eBuildleap/rest/custservice/getUserInfo",
			data : "userid=94135915886149830",
			success : function(data, textStatus, oHTTP) {
				passedModel.attr('userInfoXML', oHTTP.responseXML);
			},
			error : this.getUserInfoErrorHandler
		});
	},

	getUserInfoErrorHandler : function() {
		console.log("ERROR RETRIEVING USER INFORMATION");
	},
	
	getHomeUnits : function(passedModel){
		console.log("getHomeUnits called");
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/eBuildleap/rest/custservice/getHomeUnits",
			data : "customer="+passedModel.userId,
			success : function(data, textStatus, oHTTP) {
				passedModel.attr('homeUnitXML', oHTTP.responseXML);
			},
			error : this.getHomeUnitErrorHandler
		});
	},
	
	getHomeUnitErrorHandler : function(){
		console.log("ERROR RETRIEVING USER HOME UNIT");
	},
	
	getProject : function(passedModel){
		console.log("getProject called");
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/eBuildleap/rest/custservice/getProject",
			data : "project="+passedModel.projectId,
			success : function(data, textStatus, oHTTP) {
				passedModel.attr('projectXML', oHTTP.responseXML);
			},
			error : this.getProjectErrorHandler
		});
	},
	
	getProjectErrorHandler : function(){
		console.log("ERROR RETRIEVING PROJECT DETAILS");
	},
	
	getVersion : function(passedModel){
		console.log("getVersion called");
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/eBuildleap/rest/custservice/getVersions",
			data : "homeunit="+passedModel.homeUnitId,
			success : function(data, textStatus, oHTTP) {
				passedModel.attr('versionXML', oHTTP.responseXML);
			},
			error : this.getVersionErrorHandler
		});
	},

	getVersionErrorHandler : function(){
		console.log("ERROR RETRIEVING VERSION DETAILS");
	},

	getRevisions : function(passedModel){
		console.log("getRevisions called");
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/eBuildleap/rest/custservice/getRevisions",
			data : "version="+passedModel.versionId,
			success : function(data, textStatus, oHTTP) {
				passedModel.attr('revisionsXML', oHTTP.responseXML);
			},
			error : this.getRevisionsErrorHandler
		});
	},

	getRevisionsErrorHandler : function(){
		console.log("ERROR RETRIEVING REVISIONS DETAILS");
	},
	
	getRevision : function(passedModel){
		console.log("getRevision called");
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/eBuildleap/rest/custservice/getRevision",
			data : "revision="+passedModel.latestRevisionId,
			success : function(data, textStatus, oHTTP) {
				passedModel.attr('revisionXML', oHTTP.responseXML);
			},
			error : this.getRevisionErrorHandler
		});
	},
	

	getRevisionErrorHandler : function(){
		console.log("ERROR RETRIEVING REVISION DETAILS");
	}
});
