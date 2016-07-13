<div class="filters-container">
	<ul>
		<li>
			<ul id="env" class="filter-names">
				<li class="filter-heading-env">Environment</li>
					<ul id="ulEnv">
						<%-- <c:forEach var="env" items="${envList}">
							<li class="filter-submenu" id="envId-${env.envId}" value="${env.envId}" onclick="doFormSubmit('env',this);">${env.envName}</li>
						</c:forEach>
						<input type="button" value="click me" onclick="jiten();"/> --%>
					</ul>
			</ul>
		</li>
	</ul>
	<input type="hidden" id="envId" name="envId" value="">
</div>
<div class="filters-container">
	<ul>
		<li>
			<ul id="suite" class="filter-names">
				<li class="filter-heading-bed">Test&nbsp;Suite</li>
					<ul id="ulsuite">
						<%-- <c:forEach var="suite" items="${suiteList}">
							<li class="filter-submenu" id="suiteId-${suite.suiteId}" value="${suite.suiteId}" onclick="doFormSubmit('suite',this);">${suite.suiteName}</li>
						</c:forEach> --%>
					</ul>
			</ul>
		</li>
	</ul>
	<input type="hidden" id="suiteId" name="suiteId" value="">
</div>
<div class="filters-container no-border">
	<ul>    
		<li>
			<ul id="bed" class="filter-names">
				<li class="filter-heading-suite">Test&nbsp;Bed</li>
					<ul id="ulbed">
						<%-- <c:forEach var="bed" items="${bedList}">
							<li class="filter-submenu" id="bedId-${bed.bedId}" value="${bed.bedId}" onclick="doFormSubmit('bed',this);">${bed.bedName}</li>
						</c:forEach> --%>
					</ul>
			</ul>
		</li>
	</ul>
	<input type="hidden" id="bedId" name="bedId" value="">
	<div class="clear"></div>
</div>