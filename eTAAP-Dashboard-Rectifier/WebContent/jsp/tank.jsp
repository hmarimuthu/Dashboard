<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

	<script type="text/javascript" >
		var userStoriesJsonString = "";
		var userStoriesCountAll = "";
		var userStoriesCountCompleted = "";
	</script>
 	<c:if test="${not empty jsonString}">
	<script>
	userStoriesJsonString = ${jsonString};
		
		$(document).ready(function() {
				 userStoriesCountAll =  userStoriesJsonString.data.Sprint.userStoriesCountAll;
				 userStoriesCountCompleted = userStoriesJsonString.data.Sprint.userStoriesCountCompleted;
				 $("#total_stories").text(userStoriesCountAll);
				 $("#user_stories").text(userStoriesCountCompleted);
				 
				var completedPer = userStoriesCountCompleted / userStoriesCountAll * 100,
				
					whiteBackgroundPer = 100 - completedPer,        
				
					iG = $(".water-level-circle").attr("style"),
					inter = Number(iG.split("(")[1].split(" ")[1].split(",")[0].split("%")[0]);
						setInterval(function() {
						   if(inter > whiteBackgroundPer) {
							  inter = inter - 3;
							  backStrWebkit = "-webkit-linear-gradient(top,white " + inter + "%,#0092E7 0%)";
							  backStrMoz = "-moz-linear-gradient(top,white " + inter + "%,#0092E7 0%)";
							  backStrOpera = "-o-linear-gradient(top,white " + inter + "%,#0092E7 0%)";
							  backStrMs = "-ms-linear-gradient(top,white " + inter + "%,#0092E7 0%)";
							  $(".water-level-circle").css({"background": backStrWebkit});
							  $(".water-level-circle").css({"background": backStrMoz});
							  $(".water-level-circle").css({"background": backStrOpera});
							  $(".water-level-circle").css({"background": backStrMs});
						   }
						   return
						},200);
			  });
		</script>
	</c:if>

<p class="graph-name"><span class="graph-text-left">User Stories</span><span class="status-icon-yellow"></span></p>
<div class="iframe-container">
  <div class="container-circle">
    <div class="inner-circle">
      <div class="us-details">
      <h3 class="user-story-number"><span id="user_stories"></span></h3>
      <div class="total-us-count">
          <p class="us-count-text">OUT OF</p>
          <div class="us-horz-separator"></div>
          <h4 class="us-count" id="total_stories"></h4>
      </div>
      <div class="clear"></div>
    </div>
    <div class="clear"></div>
      <div class="water-level-circle" style="background: -webkit-linear-gradient(top,white 100%,purple 10%);background: -moz-linear-gradient(top,white 100%,purple 10%);background: -o-linear-gradient(top,white 100%,purple 10%);background: -ms-linear-gradient(top,white 100%,purple 10%);">
      </div>
        <p class="user-story-text">User Stories Completed</p>
    </div>
  </div>

</div>
