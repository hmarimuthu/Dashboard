<div class="right-container">
	<h1>Manage API</h1>
	<div class="section-container">
		<div class="pagination-section margin-bottom5">
			<div class="button-section"><a class="tooltips add-icon" href="create?paramName=sys"><span class="add-clr">Create API</span></a>&nbsp;&nbsp;<a class="tooltips remove-icon" id="deleteRecord"><span>Delete API</span></a></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=sys&page=${currentPage - 1}&orderBy=${orderBy}&orderType=${orderType}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=sys&page=${currentPage + 1}&orderBy=${orderBy}&orderType=${orderType}" class="next-active">NEXT <span class="next-btn-active"></span></a>
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
				<!--th width="200">System ID 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th-->
				<th width="250">
					<c:choose>
						<c:when test="${orderBy != 'sys_name'}">
							<span onclick="sortingHead(this,'')">API Name </span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">API Name </span> 
						</c:otherwise>
					</c:choose>
					<span class="sorting">
						<a onclick="sorting('sys_name', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('sys_name', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<th width="200">
					<c:choose>
						<c:when test="${orderBy != 'api_name'}">
							<span onclick="sortingHead(this,'')">API</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">API</span> 
						</c:otherwise>
					</c:choose>
					<span class="sorting">
						<a onclick="sorting('api_name', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('api_name', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<th width="250">
					<c:choose>
						<c:when test="${orderBy != 'url'}">
							<span onclick="sortingHead(this,'')">Url</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Url</span> 
						</c:otherwise>
					</c:choose>
					<span class="sorting">
						<a onclick="sorting('url', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('url', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<th width="240">
					<c:choose>
						<c:when test="${orderBy != 'user_id'}">
							<span onclick="sortingHead(this,'')">User ID</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">User ID</span> 
						</c:otherwise>
					</c:choose>
					<span class="sorting">
						<a onclick="sorting('user_id', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('user_id', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<!--th width="240">
					<c:choose>
						<c:when test="${orderBy != 'password'}">
							<span onclick="sortingHead(this,'')">Password</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Password</span> 
						</c:otherwise>
					</c:choose> 
					<span class="sorting">
						<a onclick="sorting('password', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('password', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th-->
				<th width="200">
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
				<!--th width="130">Last Updated 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th-->
			</tr>
			</thead>
			<tbody>
				<c:forEach var="sysList" items="${sysList}"> 
				<tr>
					<td>
						<div class="checkbox-cnt">
							<input type="checkbox" class="checkSys" id="sysId-${sysList.sysId}" value="${sysList.sysId}" <c:if test="${sysList.status == 0}">disabled</c:if> />
							<label for="sysId-${sysList.sysId}" class="<c:if test='${sysList.status == 0}'>disabled</c:if>"></label>
						</div>
					</td>
					<!--td>${sysList.sysId}</td-->
					<td><a href="edit?paramName=sys&recordId=${sysList.sysId}" id="recordId-${sysList.sysId}">${sysList.sysName}</a></td>
					<td>${sysList.apiName}</td>
					<td id="url-${sysList.sysId}">${sysList.url}</td>
					<td id="userId-${sysList.sysId}">${sysList.userId}</td>
					<!--td id="password-${sysList.sysId}">${sysList.password}</td-->
					<td><span class="${sysList.status == 1 ? 'tick-mark-icon' : 'delete-icon'}"></span></td>
					<!--td>${sysList.updatedDt}</td-->
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="pagination-section margin-top5">
			<div class="button-section"></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=sys&page=${currentPage - 1}&orderBy=${orderBy}&orderType=${orderType}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=sys&page=${currentPage + 1}&orderBy=${orderBy}&orderType=${orderType}" class="next-active">NEXT <span class="next-btn-active"></span></a>
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
	  var checkedValues = $('.checkSys:checked').map(function() {
	      return this.value;
	  }).get();
	  
	  if(checkedValues.length > 0){
	   ids = checkedValues.toString();
	   $('#deleteRecord').attr('href', 'delete?paramName=app&recordId='+ids);
	  }else{
	   alert('Please select record');
	  }
});

$('#deleteRecord').click(function () {
	var ids;
	  var checkedValues = $('.checkSys:checked').map(function() {
	      return this.value;
	  }).get();
	  
	  if(checkedValues.length > 0){
	   ids = checkedValues.toString();
	   $('#deleteRecord').attr('href', 'delete?paramName=sys&recordId='+ids);
	  }else{
	   alert('Please select record');
	  }
});

	function sorting(orderBy, orderType) {
		$('a').attr('href', 'manage?paramName=sys&page=&orderBy='+orderBy+'&orderType='+orderType);
	}

	$(document).ready(function() {
		trimText('recordId', 12);
		trimText('url', 12);
		trimText('userId', 10);
		trimText('password', 10);
	});
</script>