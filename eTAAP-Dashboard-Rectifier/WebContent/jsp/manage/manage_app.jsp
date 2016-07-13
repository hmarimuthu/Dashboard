<div class="right-container" style="width:100%">
	<h1>Manage Application</h1>
	<div class="section-container">
		<div class="pagination-section margin-bottom5">
			<div class="button-section"><a class="tooltips add-icon" href="create?paramName=app&apiId=0&deptType="><span class="add-clr">Create Application</span></a>&nbsp;&nbsp;<a class="tooltips remove-icon" id="deleteRecord"><span>Delete Application</span></a></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=app&page=${currentPage - 1}&orderBy=${orderBy}&orderType=${orderType}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=app&page=${currentPage + 1}&orderBy=${orderBy}&orderType=${orderType}" class="next-active">NEXT <span class="next-btn-active"></span></a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="next">NEXT <span class="next-btn"></span></a>
		        	</c:otherwise>
				</c:choose>
			</div>
			<div class="clear"></div>
		</div>
		<table class="table" cellpadding="0" cellspacing="0">
			<thead>
			<tr>
				<th width="20">
					<!--div class="checkbox-cnt">
						<input type="checkbox" checked="" name="check" id="checkbox1" value="None">
						<label for="checkbox1"></label>
					</div-->
				</th>
				<!--th width="200">Application ID 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th-->
				<th width="175">
					<c:choose>
						<c:when test="${orderBy != 'app_name'}">
							<span onclick="sortingHead(this,'')">Application Name</span>
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Application Name</span>
						</c:otherwise>
					</c:choose> 
					<span class="sorting">
						<a onclick="sorting('app_name', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('app_name', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<th width="75">
					<c:choose>
						<c:when test="${orderBy != 'status'}">
							<span onclick="sortingHead(this,'')">Active</span>
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Active</span>
						</c:otherwise>
					</c:choose> 
					<span class="sorting">
						<a onclick="sorting('status', 'desc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('status', 'asc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<!--th width="120">Default Environment 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th>
				<th width="115">Default Test Bed 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th>
				<th width="115">Default Test Suite 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th-->
				<th width="130">
					<c:choose>
						<c:when test="${orderBy != 'quarter_starting_month_id'}">
							<span onclick="sortingHead(this,'')">Fiscal Year Start Month</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Fiscal Year Start Month</span> 
						</c:otherwise>
					</c:choose>  
					<span class="sorting">
						<a onclick="sorting('quarter_starting_month_id', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('quarter_starting_month_id', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<!--th width="100">Last Updated 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th-->
			</tr>
			</thead>
			<tbody>
				<c:forEach var="appList" items="${app_list}"> 
				<tr>
					<td>
						<div class="checkbox-cnt">
							<input type="checkbox" class="checkApp" id="appId-${appList.appId}" value="${appList.appId}" <c:if test="${appList.status == 0}">disabled</c:if> />
							<label for="appId-${appList.appId}" class="<c:if test='${appList.status == 0}'>disabled</c:if>"></label>
						</div>
					</td>
					<!--td>${appList.appId}</td-->
					<td><a href="edit?paramName=app&recordId=${appList.appId}&apiId=0&deptType=" id="recordId-${appList.appId}">${appList.appName}</a></td>
					<td><span class="${appList.status == 1 ? 'tick-mark-icon' : 'delete-icon'}"></span></td>
					<!--td><c:out value="${appList.envName == 'null' ? '-' : appList.envName}" /></td>
					<td><c:out value="${appList.bedName == 'null' ? '-' : appList.bedName}" /></td>
					<td><c:out value="${appList.suiteName == 'null' ? '-' : appList.suiteName}" /></td-->
					<td><c:out value="${appList.monthName == 'null' ? '-' : appList.monthName}" /></td>
					<!--td>${appList.updatedDt}</td-->
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="pagination-section margin-top5">
			<div class="button-section"></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=app&page=${currentPage - 1}&orderBy=${orderBy}&orderType=${orderType}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=app&page=${currentPage + 1}&orderBy=${orderBy}&orderType=${orderType}" class="next-active">NEXT <span class="next-btn-active"></span></a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="next">NEXT <span class="next-btn"></span></a>
		        	</c:otherwise>
				</c:choose>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</div>
<script>

$('#deleteRecord').click(function () {
	var ids;
	  var checkedValues = $('.checkApp:checked').map(function() {
	      return this.value;
	  }).get();
	  
	  if(checkedValues.length > 0){
	   ids = checkedValues.toString();
	   $('#deleteRecord').attr('href', 'delete?paramName=app&recordId='+ids);
	  }else{
	   alert('Please select record');
	  }
});
	function sorting(orderBy, orderType) {
		$('a').attr('href', 'manage?paramName=app&page=&orderBy='+orderBy+'&orderType='+orderType);
	}

	$(document).ready(function() {
		trimText('recordId', 30);
	});
</script>