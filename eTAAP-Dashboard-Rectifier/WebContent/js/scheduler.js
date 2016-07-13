var intervalString="";
var apiString="";
var jobsString="";
var global=1;
var noSchedulerRecordsAvail=" No Records Available ";
var jobStatus_RUNNING="RUNNING";
var jobStatus_SUCCESS="SUCCESS";
function loadScheduleJobsContainer(){
	
	var scheduledJobs = schedulerJobsJsonVar!=undefined ? schedulerJobsJsonVar.SchedulerJobs : null;
	if(scheduledJobs!=null && scheduledJobs.length>0){
		var displayString = displayJobs(scheduledJobs);
		$('#jobsContainer').html(displayString);
	}
	else{
		noJobsAvailable();
	}
}
function noJobsAvailable(){
	$('#jobsContainer').html(noSchedulerRecordsAvail);
}
function _changeInterval(ele){
	var innerhtml = ele.innerHTML;
	document.getElementById('job_interval_'+ele.id).innerHTML=innerhtml;
	document.getElementById('job_interval_'+ele.id).style.color='#575757';
}
function changeInterval(ele){//job_interval_global1
	var innerhtml = ele.innerHTML;
	document.getElementById('job_interval_'+ele.id).innerHTML=innerhtml;
	document.getElementById('job_interval_'+ele.id).style.color='#575757';
}
function getIntervalList(pk){
	
	if(intervalList!=undefined && intervalList!=null && intervalList.length > 0){
		if(intervalString == ""){
			for(var i=0;i<=intervalList.length-1;i++){
				intervalString += "<li id='"+pk+"' value='"+intervalList[i]+"' onclick=changeInterval(this)>"+intervalList[i]+"</li>";
			}
		}
	}
	return intervalString;
}
function changeApi(ele){
	//api_name_
	var innerhtml = ele.innerHTML;
	document.getElementById('api_name_'+ele.id).innerHTML=innerhtml;
	document.getElementById('api_name_'+ele.id).style.color='#575757';
}
function getApiList(pk){
	if(apiList!=undefined && apiList!=null && apiList.length > 0){
		if(apiString == ""){
			for(var i=0;i<=apiList.length-1;i++){
				apiString += "<li id='"+pk+"' value='"+apiList[i]+"' onclick=changeApi(this)>"+apiList[i]+"</li>";
			}
		}
	}
	return apiString;
}

function getJobsList(pk){
	if(jobsList!=undefined && jobsList!=null && jobsList.length>0){
		if(jobsString == ""){
			for(var i=0;i<=jobsList.length-1;i++){
				jobsString += "<li id='actual_JOB_"+pk+"' value='"+jobsList[i]+"' onclick=javascript:changeJobName(this.innerHTML,'"+pk+"')>"+jobsList[i]+"</li>";
			}
		}
	}
	return jobsString;
}
function changeJobName(text,id){
	document.getElementById('job_child_'+id).innerHTML=text;
	document.getElementById('job_child_'+id).style.color='#575757';
}
function validateScheduledJobs(){
	//1) if (+) click --> the components should not be empty
	//2) no same jobName and apiname
	var flag = true;
	var validateArr=new Array();
	var addmoreTR = $('#addmoreTR');
	if(addmoreTR!=undefined && addmoreTR!=null && addmoreTR.length>0){
		var trTags = addmoreTR[0].childNodes;
		if(trTags!=null && trTags.length>0){
			for(var c=0;c<=trTags.length-1;c++){
				if(validateArr.length==0){
					var aa = new Array();
					var jobName= trTags[c].childNodes[1].childNodes[0].childNodes[0].innerHTML;
					var interval = trTags[c].childNodes[2].childNodes[0].childNodes[0].innerHTML;
					var apiName = trTags[c].childNodes[4].childNodes[0].childNodes[0].innerHTML;
					if(jobName=="Select"){
						alert("Please select Job");
						return flag=false;
					}else if(interval=="Select"){
						alert("Please select Interval");
						return flag=false;
					}else if(apiName=="Select"){
						alert("Please select Api");
						return flag=false;
					}
					else{
						aa[0] = jobName;
						aa[1] = apiName;
						validateArr[validateArr.length]=aa;
					}
				}else{
					// loop each index of validateArr and check with current TR
					var jobName= trTags[c].childNodes[1].childNodes[0].childNodes[0].innerHTML;
					var interval = trTags[c].childNodes[2].childNodes[0].childNodes[0].innerHTML;
					var apiName = trTags[c].childNodes[4].childNodes[0].childNodes[0].innerHTML;
					if(jobName=="Select"){
						alert("Please select Job");
						return flag=false;
					}else if(interval=="Select"){
						alert("Please select Interval");
						return flag=false;
					}else if(apiName=="Select"){
						alert("Please select Api");
						return flag=false;
					}
					else{
						var freshValidateArr = new Array();
						for(var cnt=0;cnt<=validateArr.length-1;cnt++){
							var aa = validateArr[cnt];
							var jName = aa[0];
							var aNAme = aa[1];
							var jobName = trTags[c].childNodes[1].childNodes[0].childNodes[0].innerHTML;
							var apiName = trTags[c].childNodes[4].childNodes[0].childNodes[0].innerHTML;
							if((jName == jobName) /*&& (aNAme == apiName)*/){
								alert(jName + " Job Already Scheduled");
								//alert("Already scheduled ");
								return flag = false;
							}
							if(aNAme == apiName){
								alert(aNAme + " Api Already used");
								return flag = false;
							}
							var bb = new Array();
							bb[0]=jobName;
							bb[1]=apiName;
							freshValidateArr[freshValidateArr.length]=bb;
						}
						validateArr[validateArr.length] = freshValidateArr[0];
					}
				}
			}
		}else{
			alert('Please add Jobs first!');
		}
	}else{
		alert('Please add Jobs first!');
	}
	
	return flag;
}
var deletedJobsTracerArr = new Array();
function deleteScheduledJobs(ele,isExisted){
	if(isExisted=='y'){
		deletedJobsTracerArr[deletedJobsTracerArr.length] = ele.id;
	}
}
function displayJobs(scheduledJobs){
	var jobsString=getHeaderForSchedulerJobs();
	jobsString += "<tbody id='addmoreTR'>";
	for(var i=0;i <= scheduledJobs.length-1; i++){
		var pk_jobId = scheduledJobs[i].pk_jobId;
		var jobName = scheduledJobs[i].jobName;
		var api_name = scheduledJobs[i].api_name;
		var jobStatus = scheduledJobs[i].jobStatus;
		var job_interval = scheduledJobs[i].job_interval;
		var pk_recordId = scheduledJobs[i].pk_recordId;
		var fk_jobId = scheduledJobs[i].fk_jobId;
		var executionDate = scheduledJobs[i].executionDate;
		var status = scheduledJobs[i].status==undefined ? "No status" : scheduledJobs[i].status;
		var log = scheduledJobs[i].log;

		var intervalIndex = 9000-i;
		var apiIndex = 8000-i;

		jobsString += "<tr id='tr_"+pk_jobId+"'>";
		
		jobsString += "<td>";
		if(/*status == jobStatus_RUNNING*/false){
			jobsString += "<input id='"+pk_jobId+"' type='button' class='sch-disable-dis' value='Running' /></td>";
		}else if(/*status == jobStatus_SUCCESS*/false){
			jobsString += "<input id='"+pk_jobId+"' type='button' class='sch-disable' value='Run Now' onclick='runNow(this.id)' /></td>";
		}else{
			jobsString += "<input id='"+pk_jobId+"' type='button' class='sch-disable' value='Run Now' onclick='runNow(this.id)' /></td>";
		}
		
		
		jobsString += "<td><div class='' id='parent_"+pk_jobId+"' style='width: 250px;'>";
		jobsString += "<div id='job_child_"+pk_jobId+"'>"+jobName+"</div>";
		//jobsString += "<ul class='dropdown' id='actual_JOB_ul_"+pk_jobId+"' style='z-index:100;'>";
		//jobsString += getJobsList(pk_jobId)+"</ul>";
		jobsString += "</div>";
		jobsString += (executionDate!=undefined && executionDate!="undefined") ? "&nbsp;<span class='last-run'>Last Run On : "+executionDate+"</span>" : "<span class='last-run'>Last Run On : "+"No Record Found"+"</span>";
		jobsString += "</br>"+status;
		jobsString += "</td>";
		
		jobsString += "<td>";
		jobsString += "<div class='dropdown-menu' id='interval_parent_"+pk_jobId+"' style='width: 110px;'>";
		jobsString += "<div id='job_interval_"+pk_jobId+"' style='color: #575757;'>"+job_interval+"</div>";
		jobsString += "<ul class='dropdown' id='interval_ul_' style='z-index:"+intervalIndex+"'>";
		jobsString += /*"<li id='"+job_interval+"' value='"+job_interval+" hrs'>"+job_interval+" hrs</li>"*/getIntervalList(pk_jobId);
		jobsString += "</ul>";
		jobsString += "</div>";
		jobsString += "</td>";
		
		jobsString += "<td>";
		jobsString += "<div onclick=javascript:enableDisable(this.id) id='enable_disable_"+pk_jobId+"' class='toggle'>";
		jobsString += "<div class='toggle-slide'>";
		if(jobStatus == "1"){
			jobsString += "<div style='width: 190px; margin-left: 0px;' class='toggle-inner'>";
			//jobsString += "<div style='height: 30px; width: 95px; text-indent: -15px; line-height: 30px;' class='toggle-on active'>Enable</div><div style='height: 30px; width: 30px; margin-left: -15px;' class='toggle-blob'></div><div style='height: 30px; width: 95px; margin-left: -15px; text-indent: 15px; line-height: 30px;' class='toggle-off'>Disable</div></div></div>";
			jobsString += "<div style='height: 15px; width: 45px; text-indent: -15px; line-height: 30px;' class='toggle-on active'></div><div style='height: 15px; width: 15px; margin-left: -15px;' class='toggle-blob'></div><div style='height: 30px; width: 95px; margin-left: -9px; text-indent: 15px; line-height: 30px;' class='toggle-off'></div></div></div>";
		}else{
			//jobsString += "<div class='toggle-inner' style='width: 190px; margin-left: -80px;'><div class='toggle-on' style='height: 30px; width: 95px; text-indent: -15px; line-height: 30px;'>Enable</div><div class='toggle-blob' style='height: 30px; width: 30px; margin-left: -15px;'></div><div class='toggle-off active' style='height: 30px; width: 95px; margin-left: -15px; text-indent: 15px; line-height: 30px;'>Disable</div></div>";
			jobsString += "<div class='toggle-inner' style='width: 190px; margin-left: -80px;'><div class='toggle-on' style='height: 30px; width: 95px; text-indent: -15px; line-height: 30px;'></div><div class='toggle-blob' style='height: 15px; width: 15px; margin-left: -15px;'></div><div class='toggle-off active' style='height: 15px; width: 95px; margin-left: -15px; text-indent: 15px; line-height: 30px;'></div></div>";
		}
		jobsString += "</div>";
		jobsString += "</td>";
		
		jobsString += "<td>";
		jobsString += "<div class='dropdown-menu' id='' style='width: 250px;'>";
		jobsString += "<div id='"+"api_name_"+pk_jobId+"' style='color: #575757;'>"+api_name+"</div>";
		jobsString += "<ul class='dropdown' id='actual_API_ul_' style='z-index:"+apiIndex+"'>";
		jobsString += /*"<li id='' value='"+api_name+"'>"+api_name+"</li>"*/getApiList(pk_jobId);
		jobsString += "</ul>";
		jobsString += "</div>";
		jobsString += "</td>";
		jobsString += "<!--<td><a href='#' class='tooltips remove-icon' id='"+pk_jobId+"' onclick='deleteScheduledJobs(this,y)'><span>Delete the Record</span></a></td>-->";
		
		jobsString += "</tr>";
	}
	jobsString += "</tbody>";
	jobsString += "</table>";
	return jobsString;
}

function getHeaderForSchedulerJobs(){
	var headerString = "<table class='table sch-table' cellpadding='0' cellspacing='0' id='jobsContainer_table'>";
	headerString += "<thead><tr>";
	headerString += "<th width='10%'>Run Now</th><th width='25%'>Job Name</th><th width='10%'>Interval Time (hrs)</th>";
	headerString += "<th width='11%'>Schedule Status</th><th width='25%'>Select API</th>";
	headerString += "<!--<th width='10%'>delete</th>-->";
	headerString += "</tr></thead>";
	return headerString;
}

function runNow(paramsId){
	console.log("Run Now - ID - " + paramsId);
	var uri = "runNow";
	var dataValue = "jobId="+paramsId;
	
	$.ajax({url:uri,
		type: "POST",
		data:dataValue,
		success:function(result)
			{
				retrievedRunNowResults(result,paramsId);
			}});
}
function retrievedRunNowResults(response,paramsId){
	
	if(response == 'Please Enable the Job and Save'){
		
	}else{
		$('#'+paramsId).val('Running');
		$('#'+paramsId).removeAttr('onclick');
		$('#'+paramsId).addClass('sch-disable-dis');
	}
	
	alert(response);
	
}

//function get   

function ajaxCallScheduler(){
	
}

function saveScheduledJobs(){
	var scheduledJobsString = '{"ScheduledJobs":{"ScheduledJob":[';
	var jobsContainerDiv = document.getElementById('jobsContainer');//$('#jobsContainer').html();
	var jobsContainerDivTable = jobsContainerDiv.getElementsByTagName('table');
	var tBody = jobsContainerDivTable[0].getElementsByTagName('tbody');
	var trTag = tBody[0].getElementsByTagName('tr');
	// also needs to validate --> no duplicate entries --> jobname and select_api
	for(var i=0;i<=trTag.length-1;i++){
		var tr = trTag[i];
		if(true/*-- tr className check*/){
			var tdArr = tr.getElementsByTagName('td');
			var pk;var jobName;var interval;var shdStatus;var apiName;
			/*for(var j=0;j<=tdArr.length-1;j++){
				
			}*/
			pk = tdArr[0].getElementsByTagName('input')[0].getAttribute('id');
			jobName = tdArr[1].childNodes[0].childNodes[0]/*getElementById('child_'+pk)*/.innerHTML;
			interval = tdArr[2].childNodes[0].childNodes[0].innerHTML;
			shdStatus = getStatusValue(tdArr[3]);
			apiName = tdArr[4].childNodes[0].childNodes[0].innerHTML;
			scheduledJobsString += '{"seq":"'+i+'","pk":"'+pk+'","jobName":"'+jobName+'","interval":"'+interval+'","status":"'+shdStatus+'","apiName":"'+apiName+'"}';
			if(i!=trTag.length-1)
				scheduledJobsString += ",";
		}
	}
	scheduledJobsString += ']}}';
	console.log(scheduledJobsString);
	var uri = "scheduleJobs";
	var dataValue = "request="+scheduledJobsString;
	$.ajax({url:uri,
		type: "POST",
		data:dataValue,
		dataType: "json",
		success:function(result)
			{
			scheduledJobsCallBack(result);
		}});
	
}
function getStatusValue(tdArrAt){
	var flag = 1;
	var childNodes = tdArrAt.childNodes;
	var childNodes1 = childNodes[0];
	var childNodes2 = childNodes1.childNodes[0];
	var divStyle = childNodes2.childNodes[0].getAttribute('style');
	if(/*divStyle!=undefined && divstyle!=null*/true){
		if(divStyle == 'width: 190px; margin-left: -80px;'){
			flag = 0;
		}
	}
	return flag;
}
function scheduledJobsCallBack(response){
	window.location.reload();
}
function clickDropDownMe(el){
	dropdown();
}

function addMoreSchedulers(){
/*	alert(appList);*/
	if(appList == ""){
		 alert("Please create the application before adding the record");
		} else {
			var jobIndex = 5000-global;
			var intervalIndex = 4000-global;
			var apiIndex = 3000-global;
		
			//$('#jobsContainer').html("");
			var htmlString = "<tr id='tr_global_"+global+"'>";
			htmlString += "<td><input type='button' value='Run Now' class='sch-disable' id=null style='display:none;'></td>";	
			htmlString += "<td><div style='width: 250px;' id='parent_global_"+global+"' class='dropdown-menu'>";
			htmlString += "<div id='job_child_global_"+global+"'>Select</div>";
			htmlString += "<ul style='z-index:"+jobIndex+";' id='actual_JOB_ul_global_"+global+"' class='dropdown'>";
			htmlString += _getJobsList("global_"+global)+"</ul></div>";
			htmlString += "</td>";
			
			htmlString += "<td>";
			htmlString += "<div style='width: 110px;' id='interval_parent_global_"+global+"' class='dropdown-menu'>";
			htmlString += "<div id='job_interval_global_"+global+"'>Select</div>";
			htmlString += "<ul style='z-index:"+intervalIndex+";' id='interval_ul_global_"+global+"' class='dropdown'>";
			htmlString += _getIntervalList("interval_global_"+global);
			htmlString += "</ul></div></td>";
			
			htmlString += "<td>";
			htmlString += "<div class='toggle' id='enable_disable_global_"+global+"' onclick='javascript:enableDisable(this.id)'>";
			//htmlString += "<div class='toggle-slide'><div class='toggle-inner' style='width: 190px; margin-left: 0px;'><div class='toggle-on active' style='height: 30px; width: 95px; text-indent: -15px; line-height: 30px;'>Enable</div><div class='toggle-blob' style='height: 30px; width: 30px; margin-left: -15px;'></div><div class='toggle-off' style='height: 30px; width: 95px; margin-left: -15px; text-indent: 15px; line-height: 30px;'>Disable</div></div></div></div>";
			htmlString += "<div class='toggle-slide'><div class='toggle-inner' style='width: 190px; margin-left: 0px;'><div class='toggle-on active' style='height: 30px; width: 45px; text-indent: -15px; line-height: 30px;'></div><div class='toggle-blob' style='height: 15px; width: 15px; margin-left: -15px;'></div><div class='toggle-off' style='height: 30px; width: 95px; margin-left: -9px; text-indent: 15px; line-height: 30px;'></div></div></div></div>";
			htmlString += "</td>";
			
			htmlString += "<td>";
			htmlString += "<div style='width: 250px;' id='api_parent_global_"+global+"' class='dropdown-menu'>";
			htmlString += "<div id='api_global_"+global+"'>Select</div>";
			htmlString += "<ul style='z-index:"+apiIndex+";' id='actual_API_ul_global_"+global+"' class='dropdown'>";
			htmlString += _getApiList("api_global_"+global);
			htmlString += "</ul></div>";
			htmlString += "</td>";
			
			htmlString += "<!--<td><a href='#' class='tooltips remove-icon' id='tr_global_"+global+"' onclick='deleteScheduledJobs(this,n)'><span>Delete the Record</span></a></td>-->";
			
			htmlString += "</tr>";
			
			global++;
			
			if(document.getElementById('jobsContainer').innerHTML == noSchedulerRecordsAvail){
				$('#jobsContainer').html("");
				var htmlString1 = getHeaderForSchedulerJobs() + "<tbody id='addmoreTR'>" +htmlString+ "</tbody></table>";
				$('#jobsContainer').html(htmlString1);
			}else{
				$('#addmoreTR').html($('#addmoreTR').html()+htmlString);
			}
		
			dropdown();
			dropdownAutoSize();
		}
}
function _getJobsList(pk){
	var jjString="";
	if(jobsList!=undefined && jobsList!=null && jobsList.length>0){
		if(true){
			for(var i=0;i<=jobsList.length-1;i++){
				jjString += "<li id='actual_JOB_"+pk+"' value='"+jobsList[i]+"' onclick=javascript:changeJobName(this.innerHTML,'"+pk+"')>"+jobsList[i]+"</li>";
			}
		}
	}
	return jjString;
}
function _getIntervalList(pk){
	var _intervalString="";
	if(intervalList!=undefined && intervalList!=null && intervalList.length > 0){
		if(true){
			for(var i=0;i<=intervalList.length-1;i++){
				_intervalString += "<li id='"+pk+"' value='"+intervalList[i]+"' onclick=_changeInterval(this)>"+intervalList[i]+"</li>";
			}
		}
	}
	return _intervalString;
}
function _changeApi(ele){
	var innerhtml = ele.innerHTML;
	document.getElementById(ele.id).innerHTML=innerhtml;
	document.getElementById(ele.id).style.color='#575757';
}
function _changeInterval(ele){
	var innerhtml = ele.innerHTML;
	document.getElementById('job_'+ele.id).innerHTML=innerhtml;
	document.getElementById('job_'+ele.id).style.color='#575757';
}
function _getApiList(pk){
	var _apiString="";
	if(apiList!=undefined && apiList!=null && apiList.length > 0){
		if(/*apiString == ""*/true){
			for(var i=0;i<=apiList.length-1;i++){
				_apiString += "<li id='"+pk+"' value='"+apiList[i]+"' onclick=_changeApi(this)>"+apiList[i]+"</li>";
			}
		}
	}
	return _apiString;
}
function deleteScheduledJobsRecord(paramId){
	document.getElementById('jobsContainer_table').deleteRow(0);
}

function dropdownAutoSize() {
	if (jobsList.length > 2) {
		$('[id^=actual_JOB_ul]').css('height','65px');
		$('[id^=actual_JOB_ul]').css('overflow','auto');
	} else {
		$('[id^=actual_JOB_ul]').css('height','auto');
		$('[id^=actual_JOB_ul]').css('overflow','hidden');
	}

	if (intervalList.length > 2) {
		$('[id^=interval_ul]').css('height','65px');
		$('[id^=interval_ul]').css('overflow','auto');
	} else {
		$('[id^=interval_ul]').css('height','auto');
		$('[id^=interval_ul]').css('overflow','hidden');
	}

	if (apiList.length > 2) {
		$('[id^=actual_API_ul]').css('height','65px');
		$('[id^=actual_API_ul]').css('overflow','auto');
	} else {
		$('[id^=actual_API_ul]').css('height','auto');
		$('[id^=actual_API_ul]').css('overflow','hidden');
	}
}