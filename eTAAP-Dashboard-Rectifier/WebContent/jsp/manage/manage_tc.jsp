<div class="right-container" style="width:100%">
	<h1>Manage Test Case</h1>
	<div class="section-container">
		<div class="pagination-section margin-bottom5">
			<div class="button-section"><a class="tooltips add-icon" href="create?paramName=tc"><span class="add-clr">Create Test Case</span></a>&nbsp;&nbsp;<a class="tooltips remove-icon" id="deleteRecord"><span>Delete Test Case</span></a></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=tc&page=${currentPage - 1}&orderBy=${orderBy}&orderType=${orderType}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=tc&page=${currentPage + 1}&orderBy=${orderBy}&orderType=${orderType}" class="next-active">NEXT <span class="next-btn-active"></span></a>
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
				<!--th width="200">Test Case ID 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th-->
				<th width="250">
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
				<th width="250">
					<c:choose>
						<c:when test="${orderBy != 'total_test_case_count'}">
							<span onclick="sortingHead(this,'')">Total Test Case Count</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Total Test Case Count</span> 
						</c:otherwise>
					</c:choose>
					<span class="sorting">
						<a onclick="sorting('total_test_case_count', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('total_test_case_count', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
			</tr>
			</thead>
			<tbody>
				<c:forEach var="tcList" items="${tcList}"> 
				<tr>
					<td>
						<div class="checkbox-cnt">
							<input type="checkbox" class="checkTc" id="tcId-${tcList.appId}" value="${tcList.appId}" />
							<label for="tcId-${tcList.appId}"></label>
						</div>
					</td>
					<td><a href="edit?paramName=tc&recordId=${tcList.appId}" id="recordId-${tcList.appId}">${tcList.appName}</a></td>
					<td>${tcList.tcCount}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="pagination-section margin-top5">
			<div class="button-section"></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=tc&page=${currentPage - 1}&orderBy=${orderBy}&orderType=${orderType}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=tc&page=${currentPage + 1}&orderBy=${orderBy}&orderType=${orderType}" class="next-active">NEXT <span class="next-btn-active"></span></a>
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
//checkTc:checked
	$('#deleteRecord').click(function () {
	var ids;
	  var checkedValues = $('.checkTc:checked').map(function() {
	      return this.value;
	  }).get();
	  
	  if(checkedValues.length > 0){
	   ids = checkedValues.toString();
	   $('#deleteRecord').attr('href', 'delete?paramName=tc&recordId='+ids);
	  }else{
	   alert('Please select record');
	  }
});

	function sorting(orderBy, orderType) {
		$('a').attr('href', 'manage?paramName=tc&page=&orderBy='+orderBy+'&orderType='+orderType);
	}

	$(document).ready(function() {
		trimText('recordId', 30);
	});
</script>