<h3 class="chart-heading">Velocity</h3>
<span id="selected-filter" class="filter-list"></span>
<!-- time filter starts here -->
<div class="time-filter-container">
	<div id="selected-time" class="peroid-container">
		<p></p>
	</div>
	<div id="dd" class="iteration-container">
		<h3 id="selected-period">${periodList[0].periodName}</h3>
		<ul class="iteration-options-container">
			<li class="iteration-heading"><!--Quarterly-->
				<ul id="period" class="iteration-submenu">
					<c:forEach var="period" items="${periodList}">
						<li id="periodId-${period.periodId}" startDt="${period.startDt}" endDt="${period.endDt}" value="${period.periodId}" onclick="getVelocityResultsAjaxCall('period',this);">${period.periodName}</li>
					</c:forEach>
				</ul>
			</li>
		</ul>
		<input type="hidden" id="periodId" name="periodId" value="">
		<input type="hidden" id="periodStrtDt" name="periodStrtDt" value="${periodStrtDt}">
		<input type="hidden" id="periodEndDt" name="periodEndDt" value="${periodEndDt}">
	</div>
	<div class="clear"></div>
<!-- time filter ends here -->    	
</div>
<div class="clear"></div>

<!-- charts-container starts here -->

<div id="velocity-chart-container"  class="velocity-chart-container" style="display: block;"></div>
