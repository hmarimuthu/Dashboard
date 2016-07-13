<div class="right-container">
	<h1>Manage Test Bed</h1>
	<div class="section-container">
		<div class="pagination-section margin-bottom5">
			<div class="button-section"><a class="tooltips add-icon" href="create?paramName=bed"><span class="add-clr">Create Test Bed</span></a>&nbsp;&nbsp;<a class="tooltips remove-icon" id="deleteRecord"><span>Delete Test Bed</span></a></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=bed&page=${currentPage - 1}&orderBy=${orderBy}&orderType=${orderType}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=bed&page=${currentPage + 1}&orderBy=${orderBy}&orderType=${orderType}" class="next-active">NEXT <span class="next-btn-active"></span></a>
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
				<!--th width="200">Test Bed ID 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th-->
				<th width="250">
					<c:choose>
						<c:when test="${orderBy != 'bed_name'}">
							<span onclick="sortingHead(this,'')">Test Bed Name</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Test Bed Name </span> 
						</c:otherwise>
					</c:choose>
					<span class="sorting">
						<a onclick="sorting('bed_name', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('bed_name', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<th width="165">
					<c:choose>
						<c:when test="${orderBy != 'status'}">
							<span onclick="sortingHead(this,'')">Active</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Active </span> 
						</c:otherwise>
					</c:choose> 
					<span class="sorting">
						<a onclick="sorting('status', 'desc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('status', 'asc')"><span class="down-arrow"></span></a>
					</span>
				</th>
				<th width="130">
					<c:choose>
						<c:when test="${orderBy != 'updated_dt'}">
							<span onclick="sortingHead(this,'')">Last Updated</span> 
						</c:when>
						<c:otherwise>
							<span onclick="sortingHead(this,'${orderType}')">Last Updated</span> 
						</c:otherwise>
					</c:choose> 
					<span class="sorting">
						<a onclick="sorting('updated_dt', 'asc')"><span class="up-arrow"></span></a>
						<a onclick="sorting('updated_dt', 'desc')"><span class="down-arrow"></span></a>
					</span>
				</th>
			</tr>
			</thead>
			<tbody>
				<c:forEach var="bedList" items="${bedList}"> 
				<tr>
					<td>
						<div class="checkbox-cnt">
							<input type="checkbox" class="checkBed" id="bedId-${bedList.bedId}" value="${bedList.bedId}" <c:if test="${bedList.status == 0}">disabled</c:if> />
							<label for="bedId-${bedList.bedId}" class="<c:if test='${bedList.status == 0}'>disabled</c:if>"></label>
						</div>
					</td>
					<!--td>${bedList.bedId}</td-->
					<td><a href="edit?paramName=bed&recordId=${bedList.bedId}" id="recordId-${bedList.bedId}">${bedList.bedName}</a></td>
					<td><span class="${bedList.status == 1 ? 'tick-mark-icon' : 'delete-icon'}"></span></td>
					<td>${bedList.updatedDt}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="pagination-section margin-top5">
			<div class="button-section"></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=bed&page=${currentPage - 1}&orderBy=${orderBy}&orderType=${orderType}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=bed&page=${currentPage + 1}&orderBy=${orderBy}&orderType=${orderType}" class="next-active">NEXT <span class="next-btn-active"></span></a>
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
				  var checkedValues = $('.checkBed:checked').map(function() {
				      return this.value;
				  }).get();
				  
				  if(checkedValues.length > 0){
				   ids = checkedValues.toString();
				   $('#deleteRecord').attr('href', 'delete?paramName=bed&recordId='+ids);
				  }else{
				   alert('Please select record');
				  }
			});



	function sorting(orderBy, orderType) {
		$('a').attr('href', 'manage?paramName=bed&page=&orderBy='+orderBy+'&orderType='+orderType);
	}

	$(document).ready(function() {
		trimText('recordId', 30);
	});
</script>