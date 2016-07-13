<%@page import="java.util.ArrayList"%>
<%@page import="com.etaap.domain.TestCase"%>

<div class="right-container" style="height:100%; width:100%">
	<%
		Object tcDetails = request.getAttribute("tcDetails");
		String title = "";
		String actionUrl = "";
		if(tcDetails != null) {
			title = "Edit Test Case Data";
			actionUrl = "update?paramName=tc";
		} else {
			title = "Create New Test Case Data";
			actionUrl = "create?paramName=tc";
		}
	%>
	<h1><%= title %></h1>
	<div class="form-container section-container">
	<form:form id="create_form" action="<%= actionUrl %>" method="post" modelAttribute="testCase">
		<div class="form-elements">
			<div class="label-name">Application<sup class="req">*</sup>:</div>
			<div class="input-field">
				<div class="dropdown-menu" id="app_dd">
					<div id="selected_appId">Select</div>
					<ul class="dropdown app" id="app_list" style="z-index:10000;">
					
						<c:forEach var="app" items="${app_list}">
							<li id="appId-${app.appId}" value="${app.appId}">${app.appName}</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<!--div class="form-elements">
			<div class="label-name">Quarter<sup class="req">*</sup>:</div>
			<div class="input-field">
				<div class="dropdown-menu" id="quarter_dd" style="width:280px;">
					<div id="selected_quarterId">Select</div>
					<ul class="dropdown auqrter" id="quarter_list" style="z-index:9500;">
					</ul>
				</div>
			</div>
			<div class="clear"></div>
		</div-->
		<h3 class="h3-title">Details</h3>
		<div id="system-dtls-cnt" class="system-dtls-cnt">
			<%
				if (tcDetails != null) {
			%>
			<div class="system-dtls-tbl">
				<div class="form-elements creat-prop-lay">
					<section class="tabs">
						<div class="tab-list">
						
							<c:forEach var="tabs" items="${tabs}">
								<div class="tab-name test-case-tab" id="tab_name${tabs.key}" value="${tabs.key}" title="${tabs.value}">
									<input type="radio" class="tab-selector-${tabs.key}" id="tab-${tabs.key}" name="radio-set" value="${tabs.key}"/>
									<label for="tab-${tabs.key}" class="tab-label tab-label-${tabs.key}" name="${tabs.value}"></label>${tabs.value}
								</div>
							</c:forEach>
						</div>
						<div class="clear"></div>

						<div class="tab-content">
							<c:forEach var="tabs" items="${tabs}">
								<div id="div_${tabs.key}" class="input-field form-elements tab-hide content">
									<table id="system-dtls-tbl-${tabs.key}">
										<tr>
											<th>Suite Name</th>
											<th>Test Case Type</th>
											<th>Count</th>
											<th><span class="addRows" onclick="addRow('${tabs.key}');" title="Add New Row"></span></th>
										</tr>

										<c:forEach var="tabId" items="${tcDetails.tabIdList}" varStatus="status">
											<c:if test="${tabs.key == tabId}">
												<tr class="system-dtls">
													<td>
														<div class="dropdown-menu" id="suite_dd${tcDetails.idList[status.index]}" style="width: 150px;">
															<div id="selected_suiteId${tcDetails.idList[status.index]}" suiteid="${tcDetails.suiteIdList[status.index]}">Select</div>
															<ul class="dropdown suite" id="suite_list${tcDetails.idList[status.index]}" style="z-index:${10000-status.index};">
																<c:forEach var="suite" items="${_suite_list}">
																	<li id="suiteId-${suite.suiteId}" value="${suite.suiteId}">${suite.suiteName}</li>
																</c:forEach>
															</ul>
														</div>
														<div class="clear"></div>
													</td>
													<td>
														<div class="dropdown-menu" id="tcType_dd${tcDetails.idList[status.index]}" style="width: 150px;">
															<div id="selected_tcType${tcDetails.idList[status.index]}" tcType="${tcDetails.tcTypeList[status.index]}">Select</div>
															<ul class="dropdown tcType" id="tcType_list${tcDetails.idList[status.index]}" style="z-index:${9000-status.index};">
																<c:forEach var="tcType" items="${tcType_list}">
																	<li id="tcType-${tcType.key}" value="${tcType.key}">${tcType.value}</li>
																</c:forEach>
															</ul>
														</div>
														<div class="clear"></div>
													</td>
													<td>
														<input type="text" id="tcCount${tcDetails.idList[status.index]}" name="tcCount" style="width: 75px;" value="${tcDetails.tcCountList[status.index]}" />
														<input type="hidden" id="tabId" name="tabIds" value="${tcDetails.tabIdList[status.index]}" />
														<input type="hidden" id="id" name="ids" value="${tcDetails.idList[status.index]}" />
														<input type="hidden" id="recordType${tcDetails.idList[status.index]}" name="recordType" value="nochange" />
													</td>
													<td><span id="${tcDetails.idList[status.index]}" class="removeRow" title="Delete"></span></td>
													<td><span id="${tcDetails.idList[status.index]}" class="editRow" title="Update"></span></td>
												</tr>
											</c:if>
										</c:forEach>
									</table>
								</div>
								<div class="clear"></div>
							</c:forEach>
                            <div class="clear"></div>
                            <p class="note"></p>
						</div>
	                	<div class="clear"></div>
					</section>
					<div class="clear"></div>
				</div>
			</div>
			<%
				} else {
			%>
			<div class="system-dtls-tbl">
				<div class="form-elements creat-prop-lay">
					<section class="tabs">
						<div class="tab-list">
							<c:forEach var="tabs" items="${tabs}">
								<div class="tab-name" id="tab_name${tabs.key}" value="${tabs.key}" title="${tabs.value}">
									<input type="radio" class="tab-selector-${tabs.key}" id="tab-${tabs.key}" name="radio-set" value="${tabs.key}"/>
									<label for="tab-${tabs.key}" class="tab-label tab-label-${tabs.key}" name="${tabs.value}"></label>${tabs.value}
								</div>
							</c:forEach>
						</div>
						<div class="clear"></div>

						<div class="tab-content">
							<c:forEach var="tabs" items="${tabs}">
								<div id="div_${tabs.key}" class="input-field form-elements tab-hide content">
									<table id="system-dtls-tbl-${tabs.key}">
										<tr>
											<th>Suite Name</th>
											<th>Test Case Type</th>
											<th>Count</th>
											<th><span class="addRows" onclick="addRow('${tabs.key}');" title="Add New Row"></span></th>
										</tr>

										<!--tr class="system-dtls">
											<td>
												<div class="dropdown-menu" id="suite_dd${tabs.key}" style="width: 150px;">
													<div id="selected_suiteId${tabs.key}" suiteid="0">Select</div>
													<ul class="dropdown suite" id="suite_list${tabs.key}" style="z-index:10000;">
														<c:forEach var="suite" items="${suite_list}">
															<li id="suiteId-${suite.suiteId}" value="${suite.suiteId}">${suite.suiteName}</li>
														</c:forEach>
													</ul>
												</div>
												<div class="clear"></div>
											</td>
											<td>
												<div class="dropdown-menu" id="tcType_dd${tabs.key}" style="width: 150px;">
													<div id="selected_tcType${tabs.key}" tcType="0">Select</div>
													<ul class="dropdown tcType" id="tcType_list${tabs.key}" style="z-index:9000;">
														<c:forEach var="tcType" items="${tcType_list}">
															<li id="tcType-${tcType.key}" value="${tcType.key}">${tcType.value}</li>
														</c:forEach>
													</ul>
												</div>
												<div class="clear"></div>
											</td>
											<td>
												<input type="text" id="tcCount${tabs.key}" name="tcCount" style="width: 75px;" value="" />
												<input type="hidden" id="tabId" name="tabIds" value="${tabs.key}" />
											</td>
											<td><span class="removeRow" title="Delete Row"></span></td>
										</tr-->
									</table>
								</div>
								<div class="clear"></div>
							</c:forEach>
                            <div class="clear"></div>
                            <p class="note"></p>
						</div>
	                	<div class="clear"></div>
					</section>
					<div class="clear"></div>
				</div>
			</div>
			<% } %>
		</div>
		<div class="clear"></div>

		<form:input type="hidden" id="app_id" path="appId" value="" />
		<form:input type="hidden" id="suite_id" path="suiteIds" value="" />
		<form:input type="hidden" id="tcTypes" path="tcTypes" value="" />
		<div class="btn-container">
			<input type="button" id="createBtn" class="btn-blue" value="SAVE"><input type="button" id="cancelBtn" class="btn-red" value="CANCEL">
		</div>
	</form:form>
	</div>
</div>
<script type="text/javascript">
	$('#app_list li').click(function(event) {
		$('#selected_appId').text($(this).text()).css('color', '#575757');
		$('#app_id').val($(this).val());
		$(this).parent().parent().removeClass("active");
		event.stopPropagation();
		GetPeriodList($(this).val());
	});

	function GetPeriodList(app_id) {
		var uri = contextPath + "/GetQuarterList?";
	    $.ajax({
	        type: "POST",
	        url: uri,
	        data: "app_id=" + app_id,
	        dataType: "json",
	        success: function (result) {
	            populateListItems(result);
	        },
	        "error": function (result) {
	            var response = result.responseText;
	            alert('Error loading: ' + response);
	        }
	    });
	}

	function populateListItems(list) {
		var quarterStr = '';
	    $.each(list, function(i, e) {
			$.each(e.QuarterList, function(index, element) {
				$('#tab_name'+element.periodId).text(element.periodName);
				$('#tab_name'+element.periodId).attr("title", dateField(element.startDt, element.endDt));
			});
	    });
	    $('#quarter_list').html(quarterStr);
	}

	function dateField(startDt, endDt) {
        var startDate = new Date(startDt.substring(0, startDt.indexOf(" ")));
        var endDate = new Date(endDt.substring(0, endDt.indexOf(" ")));
        var sd = startDate.toDateString();
        var ed = endDate.toDateString();

        return (sd.substring(sd.indexOf(" ")) + " - " + ed.substring(ed.indexOf(" ")));
	}

	$('[id^=suite_list]').on('click', 'li', function(event) {
		$(this).parent().siblings("[id^=selected_suiteId]").text($(this).text()).css('color', '#575757');
		$(this).parent().siblings("[id^=selected_suiteId]").attr("suiteid", $(this).val());
		$(this).parent().parent().removeClass("active");
		$('.dropdown-menu').css('border','');
		event.stopPropagation();
	});
	$('[id^=tcType_list]').on('click', 'li', function(event) {
		$(this).parent().siblings("[id^=selected_tcType]").text($(this).text()).css('color', '#575757');
		$(this).parent().siblings("[id^=selected_tcType]").attr("tctype", $(this).text());
		$(this).parent().parent().removeClass("active");
		$('.dropdown-menu').css('border','');
		event.stopPropagation();
	});
	$('[id^=tcCount]').on('click', function() {
		$('[id^=tcCount]').css('border', '');
	});
	$('[id^=tcCount]').keypress(function(key) {
		return numChkOnKeyPress(key);
	});
	$('[id^=tcCount]').on("cut copy paste", null,function(e) {
		e.preventDefault();
	});

	$('#createBtn').click(function () {
		// Condition to check whether last record is deleted or not
		if ('${tcDetails}') {
			if ($("input[name='recordType']").length == $("input[name='recordType'][value='delete']").length) {
				if (confirm('Deleting all record will remove the application from the list')) {

				} else {
					return;
				}
			}
		}

		if ($('#app_id').val() != 0) {
			var suiteid_list = $('[id^=selected_suiteId]').map(function(){return $(this).attr("suiteid");}).get();
			var tcType_list = $('[id^=selected_tcType]').map(function(){return $(this).attr("tcType");}).get();
			$('#suite_id').val(suiteid_list);
			$('#tcTypes').val(tcType_list);
			
			//alert($('#suite_id').val());
			//alert($('#tcTypes').val());
			//var tabId_list = $('[id^=tabId]').map(function(){return $(this).attr("value");}).get();
			//var tcCount_list = $('[id^=tcCount]').map(function(){return $(this).attr("value");}).get();
			//alert(tabId_list);
			//alert(tcCount_list);

			if (validate()) {
				$("#create_form").submit();
			}
		}
		else {
			if ($('#app_id').val() == 0) {
				alert("Select Application");
				return;
			}
		}
	});
	
	$('#cancelBtn').click(function () {
		window.history.back();
	});

	function validate() {
		var tabId_list = $('[id^=tabId]').map(function(){return $(this).attr("value");}).get();
		var suiteid_id_list = $('[id^=selected_suiteId]').map(function(){return $(this).parent().attr("id");}).get();
		var suiteid_list = $('[id^=selected_suiteId]').map(function(){return $(this).attr("suiteid");}).get();
		var tcType_id_list = $('[id^=selected_tcType]').map(function(){return $(this).parent().attr("id");}).get();
		var tcType_list = $('[id^=selected_tcType]').map(function(){return $(this).attr("tcType");}).get();
		var tcCount_id_list = $('[id^=tcCount]').map(function(){return $(this).attr("id");}).get();
		var tcCount_list = $('[id^=tcCount]').map(function(){return $(this).attr("value");}).get();

		if (tabId_list.length > 0) {
			for (var i=0; i<tabId_list.length; i++) {
				var tabid1 = tabId_list[i];
				var suite1 = suiteid_list[i];
				var tcType1 = tcType_list[i];
				var tcCount1 = tcCount_list[i];

				if (suite1==0) {
					alert("Select Suite Name");
					$("#"+suiteid_id_list[i]).css('border', '1px solid red');
					return false;
				} else if (tcType1==0) {
					alert("Enter Test Case Type");
					$("#"+tcType_id_list[i]).css('border', '1px solid red');
					return false;
				} else if (tcCount1=="" || tcCount1<0) {
					alert("Enter Test Case Count");
					$("#"+tcCount_id_list[i]).css('border', '1px solid red');
					return false;
				}

				for (var j=i+1; j<tabId_list.length; j++) {
					var tabid2 = tabId_list[j];
					var suite2 = suiteid_list[j];
					var tcType2 = tcType_list[j];
					
					if (tabid1==tabid2 && suite1==suite2 && tcType1==tcType2) {
						alert("Same combination of Suite Name and Test Case Type is not allowed");
						$("#"+suiteid_id_list[i]).css('border', '1px solid red');
						$("#"+tcType_id_list[i]).css('border', '1px solid red');
						$("#"+suiteid_id_list[j]).css('border', '1px solid red');
						$("#"+tcType_id_list[j]).css('border', '1px solid red');
						return false;
					}
				}
			}
		} else {
			alert("Enter at least one Record");
			return false;
		}

		return true;
	}

	$(document).ready(function() {
		if ('${tcDetails}') {
			<c:forEach items="${app_list}" var="app_list">
				<c:if test="${app_list.appId==tcDetails.appId}">
					$('#selected_appId').text('${tcDetails.appName}').css('color', '#575757');
					$('#app_id').val('${tcDetails.appId}');
					$('#app_dd').removeClass("dropdown-menu").addClass("dropdown-menu-disable");
					$('#app_dd').css("cursor", "default");
				</c:if>
			</c:forEach>

			<c:forEach var="tabs" items="${tabs}">
				<c:if test="${tabs.key == tcDetails.quarterList[tabs.key].periodId}">
					$('#tab_name'+'${tcDetails.quarterList[tabs.key].periodId}').text('${tcDetails.quarterList[tabs.key].periodName}');
					$('#tab_name'+'${tcDetails.quarterList[tabs.key].periodId}').attr("title", dateField('${tcDetails.quarterList[tabs.key].startDt}', '${tcDetails.quarterList[tabs.key].endDt}'));
				</c:if>

				<c:forEach var="tabId" items="${tcDetails.tabIdList}" varStatus="status">
					<c:if test="${tabs.key == tabId}">
						<c:forEach items="${_suite_list}" var="suite_list">
							<c:if test="${suite_list.suiteId == tcDetails.suiteIdList[status.index]}">
								$('#selected_suiteId'+'${tcDetails.idList[status.index]}').text('${suite_list.suiteName}').css('color', '#575757');
							</c:if>
						</c:forEach>
						<c:forEach items="${tcType_list}" var="tcType_list">
							<c:if test="${tcType_list.key == tcDetails.tcTypeList[status.index]}">
								$('#selected_tcType'+'${tcDetails.idList[status.index]}').text('${tcType_list.value}').css('color', '#575757');
							</c:if>
						</c:forEach>
					</c:if>
				</c:forEach>
			</c:forEach>
		}

		$('.removeRow').on("click", null,function() {
			var attrThisId = $(this).attr("id");
			if (attrThisId && attrThisId !=0) {
				//if(confirm("Record will be deleted from database.\nAre you sure you want to delete this?")) {
				if(confirm("Are you sure you want to delete this?")) {
					//deleteUpdateTCM("delete", $(this).attr("id"));
					
					$('#suite_dd'+attrThisId+',#tcType_dd'+attrThisId+',#tcCount'+attrThisId).removeClass('view-deleted').addClass('view-deleted');
					$('#recordType'+attrThisId).val('delete');
				}
			} else {
				$(this).parent().parent().remove();
			}
		});
		
		$('.editRow').on("click", null,function() {
			var attrThisId = $(this).attr("id");
			if (attrThisId && attrThisId !=0) {
				//if(confirm("Record will be updated in database.\nAre you sure you want to update this?")) {
				if(confirm("Are you sure you want to update this?")) {
					//if (validate()) {
						//deleteUpdateTCM("update", $(this).attr("id"));
						$('#suite_dd'+attrThisId+',#tcType_dd'+attrThisId+',#tcCount'+attrThisId).removeClass('view-deleted');
						$('#recordType'+attrThisId).val('update');
					//}
				}
			}
		});

		dropdown();
		dropdownAutoSize();

		// js for tab navigation
		$('[id^=div_]').first().removeClass("tab-hide");
		$('[id^=div_]').first().addClass("current");
		$(".tab-name:eq(0)").addClass('active-tab');
		$('[id^=tab_name]').on('click', function() {
			$(".tab-name").removeClass('active-tab');
			$(this).addClass('active-tab');

			$(".content").removeClass('current');
			$(".content").addClass('tab-hide');
			$("#div_"+$(this).attr("value")).removeClass('tab-hide');
			$("#div_"+$(this).attr("value")).addClass('current');
		});
	});

	function deleteUpdateTCM(action, recordId) {
		var suiteId = $('#selected_suiteId'+recordId).attr("suiteid");
		var tcType = $('#selected_tcType'+recordId).attr("tcType");;
		var tcCount = $('#tcCount'+recordId).val();

		var uri = contextPath + "/deleteUpdateTCMByAjaxCall?action="+action;
		var param = "&recordId=" + recordId + "&suiteId=" + suiteId + "&tcType=" + tcType + "&tcCount=" + tcCount;

	    $.ajax({
	        type: "POST",
	        url: uri,
	        data: param,
	        dataType: "text",
	        success: function (result) {
	            alert(result);
	            if (action == "delete")
	            	$("#"+recordId).parent().parent().remove();
	        },
	        "error": function (result) {
	            var response = result.responseText;
	            alert('Error loading: ' + response);
	        }
	    });
	}

	var i = 1000;
	function addRow(id) {
		var div = document.createElement('tr');
		div.className = 'system-dtls';
		div.innerHTML = '<td>\
					<div class="dropdown-menu" id="suite_ddd" style="width: 150px;">\
						<div id="selected_suiteIdd" suiteid="0">Select</div>\
						<ul class="dropdown suite" id="suite_listd">\
							<c:forEach var="suite" items="${suite_list}">\
								<li id="suiteId-${suite.suiteId}" value="${suite.suiteId}">${suite.suiteName}</li>\
							</c:forEach>\
						</ul>\
					</div>\
					<div class="clear"></div>\
				</td>\
				<td>\
					<div class="dropdown-menu" id="tcType_ddd" style="width: 150px;">\
						<div id="selected_tcTyped" tctype="0">Select</div>\
						<ul class="dropdown tcType" id="tcType_listd">\
							<c:forEach var="tcType" items="${tcType_list}">\
							<li id="tcType-${tcType.key}" value="${tcType.key}">${tcType.value}</li>\
							</c:forEach>\
						</ul>\
					</div>\
					<div class="clear"></div>\
				</td>\
				<td><input type="text" id="tcCount" name="tcCount" value="0" style="width:75px;" />'+
					'<input type="hidden" id="tabId" name="tabIds" value="'+id+'" />\
					<input type="hidden" id="id" name="ids" value="0" />\
					<input type="hidden" id="recordType" name="recordType" value="new" />\
				</td>\
				<td><span class="removeRow" title="Delete Row"></span></td>';

		document.getElementById('system-dtls-tbl-'+id).appendChild(div);

		$('#suite_ddd').css('z-index', 10000-i);
	 	$('#suite_ddd').attr('id', 'suite_dd'+i);
	 	$('#suite_listd').attr('id', 'suite_list'+i);
	 	$('#selected_suiteIdd').attr('id', 'selected_suiteId'+i);
		$('#tcType_ddd').css('z-index', 9000-i);
		$('#tcType_ddd').attr('id', 'tcType_dd'+i);
		$('#tcType_listd').attr('id', 'tcType_list'+i);
		$('#selected_tcTyped').attr('id', 'selected_tcType'+i);
		$('#tcCount').attr('id', 'tcCount'+i);
		$('#recordType').attr('id', 'recordType'+i);

		$('#suite_list'+i).on('click', 'li', function(event) {
			$(this).parent().siblings("[id^=selected_suiteId]").text($(this).text()).css('color', '#575757');
			$(this).parent().siblings("[id^=selected_suiteId]").attr("suiteid", $(this).val());
			$(this).parent().parent().removeClass("active");
			$('.dropdown-menu').css('border','');
			event.stopPropagation();
		});
		$('#tcType_list'+i).on('click', 'li', function(event) {
			$(this).parent().siblings("[id^=selected_tcType]").text($(this).text()).css('color', '#575757');
			$(this).parent().siblings("[id^=selected_tcType]").attr("tctype", $(this).text());
			$(this).parent().parent().removeClass("active");
			$('.dropdown-menu').css('border','');
			event.stopPropagation();
		});
		$('#tcCount'+i).on('click', function() {
			$('[id^=tcCount]').css('border', '');
		});
		$('[id^=tcCount]').keypress(function(key) {
			return numChkOnKeyPress(key);
		});
		$('[id^=tcCount]').on("cut copy paste",null, function(e) {
			e.preventDefault();
		});

		dropdownAutoSize();
		i++;
		dropdown();
	}

	function dropdown() {
		function DropDown(el) {
			this.dd = el;
			this.initEvents();
		}

		/*DropDown.prototype = {
			initEvents : function() {
				var obj = this;
				obj.dd.on('click', function(event) {
				$(this).addClass('active');
					event.stopPropagation();
				});
			}
		};*/

		DropDown.prototype = {
			initEvents : function() {
				var obj = this;
				obj.dd.off('click').on('click', function(event) {
					var classRef = $(this).attr('class');
					$('.dropdown-menu').each(function() {
						if($(this).attr('class') != classRef) {
							$(this).removeClass('active');
						}
						$('.settings').removeClass('active');
					});
					$(this).toggleClass('active');
					event.stopPropagation();
				});
			}
		};

		$(function() {
			var dd = new DropDown($('.dropdown-menu'));
			$(document).click(function() {
				$('.dropdown-menu').removeClass('active');
			});
		});
	}

	function dropdownAutoSize() {
		if ('${fn:length(app_list)}' > 5) {
			$('.app').css('height','195px');
			$('.app').css('overflow','auto');
		} else {
			$('.app').css('height','auto');
			$('.app').css('overflow','hidden');
		}
		if ('${fn:length(suite_list)}' > 5) {
			$('.suite').css('height','195px');
			$('.suite').css('overflow','auto');
		} else {
			$('.suite').css('height','auto');
			$('.suite').css('overflow','hidden');
		}
		if ('${fn:length(tcType_list)}' > 5) {
			$('.tcType').css('height','195px');
			$('.tcType').css('overflow','auto');
		} else {
			$('.tcType').css('height','auto');
			$('.tcType').css('overflow','hidden');
		}
	}
	
	function numChkOnKeyPress(key) {
		// Opera 8.0+ (UA detection to detect Blink/v8-powered Opera)
		var isOpera = !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
		// Firefox 1.0+
		var isFirefox = typeof InstallTrigger !== 'undefined';
		// At least Safari 3+: "[object HTMLElementConstructor]"
		var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
		// Chrome 1+
		var isChrome = !!window.chrome && !isOpera;
		// At least IE6
		var isIE = false || !!document.documentMode;

		if (isFirefox) {
			if(key.keyCode == 8 || key.keyCode == 46 || (key.keyCode > 36 && key.keyCode < 41) || (key.ctrlKey && key.charCode == 122))
				return true;
		}
		if(key.charCode < 48 || key.charCode > 57)
			return false;
	}
</script>