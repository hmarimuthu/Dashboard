<div class="right-container">
	<h1>Manage Time Period</h1>
	<div class="section-container">
		<div class="pagination-section margin-bottom5">
			<div class="button-section"><a class="tooltips add-icon" href="create?paramName=period"><span class="add-clr">Add Record</span></a>&nbsp;&nbsp;<a class="tooltips remove-icon" id="deleteRecord"><span>Delete Record</span></a></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=period&page=${currentPage - 1}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=period&page=${currentPage + 1}" class="next-active">NEXT <span class="next-btn-active"></span></a>
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
				<!--th width="200">Time Period ID 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th-->
				<th width="250">Property Name 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th>
				<th width="165">Month Name 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th>
				<th width="130">Last Updated 
					<span class="sorting">
						<span class="up-arrow"></span>
						<span class="down-arrow"></span>
					</span>
				</th>
			</tr>
			</thead>
			<tbody>
				<c:forEach var="periodList" items="${periodList}"> 
				<tr>
					<td>
						<div class="checkbox-cnt">
							<input type="checkbox" class="checkPeriod" id="periodId-${periodList.periodId}" value="${periodList.periodId}">
							<label for="periodId-${periodList.periodId}"></label>
						</div>
					</td>
					<!--td>${periodList.periodId}</td-->
					<td><a href="edit?paramName=period&recordId=${periodList.periodId}">${periodList.appName}</a></td>
					<td>${periodList.monthName}</td>
					<td>${periodList.updatedDt}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="pagination-section margin-top5">
			<div class="button-section"></div>
			<div class="pagination">
				<c:choose>
					<c:when test="${currentPage != 1}">
		        		<a href="manage?paramName=period&page=${currentPage - 1}" class="prev-active"><span class="previous-btn-active"></span> PREV</a>
		        	</c:when>
		        	<c:otherwise>
		        		<a href="#" class="prev"><span class="previous-btn"></span> PREV</a>
		        	</c:otherwise>
	        	</c:choose>
				<span class="result-text">Page ${currentPage} of ${noOfPages}</span>
				<c:choose>
					<c:when test="${currentPage lt noOfPages}">
		        		<a href="manage?paramName=period&page=${currentPage + 1}" class="next-active">NEXT <span class="next-btn-active"></span></a>
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
		if ($('.checkPeriod:checked').length) {
			if ($('.checkPeriod:checked').length > 1) {
		    	alert('One selection allowed');
		    }
		    else {
		      var chkId = $('.checkPeriod:checked').val();
		      $('#deleteRecord').attr('href', 'delete?paramName=period&recordId='+chkId);
		    }
	    }
	    else {
	      alert('Please select record');
	    }
	});
</script>