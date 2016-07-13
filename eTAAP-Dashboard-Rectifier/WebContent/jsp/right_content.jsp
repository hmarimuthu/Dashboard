<h3 class="chart-heading" id="displayName">CI Results :&nbsp;</h3>
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
						<li id="periodId-${period.periodId}" startDt="${period.startDt}" endDt="${period.endDt}" value="${period.periodId}" onclick="getCiResultsAjaxCall('period',this);">${period.periodName}</li>
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

<div class="clear"></div>
<div id="ci-chart-container" class="ci-chart-container" style="display: block;"></div>
<div id="defects-chart-container" class="ci-chart-container" style="display:none;">
	<div id="severity-chart-container" class="pie-chart-div pie-chart-div-sep"></div>
	<div id="status-chart-container" class="pie-chart-div"></div>
</div>
<div id="tcm-chart-container" class="ci-chart-container" style="display: none;"></div>
	            <!--div class="chart-content-container">
					<h3 class="chart-heading">TOTAL DEFECTS</h3>
					<div class="chart-container">
						<div class="total-defects-chart"></div>
	                        <div class="defect-details">
	                        	<ul class="legend-container">
	                        		<li class="open"><span>Open</span></li>
	                            	<li class="closed"><span>Closed</span></li>
	                            	<li class="verified"><span>Verified</span></li>
	                       		</ul>
	                        	<div class="total-defects">20<p>Defects</p></div>
							</div>
						<div class="clear"></div>
					</div>
	            </div>
				<div class="chart-content-container">
					<h3 class="chart-heading">OPEN DEFECTS</h3>
	                <div class="chart-container">
					<div class="open-defects-chart"></div>
						<div class="defect-details">
							<ul class="legend-container">
								<li class="open"><span>Sprint1</span></li>
	                            <li class="closed"><span>Sprint2</span></li>
	                            <li class="verified"><span>Sprint3</span></li>
	                       	</ul>
	                        <div class="total-defects">14<p>Defects</p></div>
						</div>
						<div class="clear"></div>
					</div>
				</div-->
				<!-- charts-container ends here -->