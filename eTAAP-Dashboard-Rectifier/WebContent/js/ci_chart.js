function ciChart(buildNumberCSV, passCountCSV, failCountCSV, skipCountCSV, buildDateCSV) {
	
	var b = /*buildNumberCSV.toString().split(',').map(function(item) {
	    item = item.replace(/['"]/g, '')
		return item;
	});*/buildNumberCSV;
	
	
	if (passCountCSV != undefined) {
		var c = passCountCSV.toString().split(',').map(function(item) {
		    return parseInt(item, 10);
		});
		var d = failCountCSV.toString().split(',').map(function(item) {
		    return parseInt(item, 10);
		});
		var e = skipCountCSV.toString().split(',').map(function(item) {
		    return parseInt(item, 10);
		});
	}

	/*var perShapeGradient = {
		x1 : 0,
		y1 : 0,
		x2 : 1,
		y2 : 1
	};*/

	if (buildNumberCSV != undefined && buildNumberCSV.length > 0) {
		$('#ci-chart-container').highcharts({
			chart : {
				type : 'column',
				width : 780,
				height : 380,
				zoomType: 'x',
				style: {
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				},
			},
			/*colors : [
					{
						linearGradient : perShapeGradient,
						stops : [ [ 0, 'rgb(136, 219, 5)' ],
								[ 1, 'rgb(86, 138, 3)' ] ]
					},
					{
						linearGradient : perShapeGradient,
						stops : [ [ 0, 'rgb(255, 120, 133)' ],
								[ 1, 'rgb(182, 49, 49)' ] ]
					},
					{
						linearGradient : perShapeGradient,
						stops : [ [ 0, 'rgb(252, 247, 119)' ],
								[ 1, 'rgb(241, 218, 54)' ] ]
					} ],*/
			colors : ['#a9ce8e', '#ff6666', '#ffcc66'],
			tooltip : {
				enabled : false
			},
			title : {
				text : ''
			},
			subtitle : {
				text : ''
			},
			xAxis : {
				categories : buildDateCSV,
				title : {
					text : 'Build Date'
				},
			    labels: {
			        formatter: function() {
						var thedate = Highcharts.dateFormat('%b %e', new Date(this.value));
	                    return thedate;
			        },
					rotation: -45
			    }
			},
			yAxis : {
				min : 0,
				tickInterval : 2,
				title : {
					text : 'Test Case Results'
				}
			},
			/*tooltip : {
				backgroundColor : {
					linearGradient : {
						x1 : 0,
						y1 : 0,
						x2 : 0,
						y2 : 1
					},
					stops : [ [ 0, 'white' ], [ 1, '#EEE' ] ]
				},
				borderColor : 'gray',
				borderWidth : 1,
				headerFormat : '<span style="padding: 15px 0 5px 10px; display: block; font-size: 12px; line-height:13px;">Build #<span></span>{point.key}</span><table>',
				pointFormat : '<tr><td style="padding:0 0 0 10px; display: block;font-size: 12px; margin:0; line-height:13px;">{series.name}:</td><td style="margin:0;padding:0; line-height:13px;">{point.y:f}</td></tr>',
				footerFormat : '</table>',
				shared : true,
				useHTML : true,
				enabled : true
			},*/
			tooltip:{
				borderRadius: 2,
			    borderWidth: 1,
			    borderColor: '#999',
			    shadow: false,
			    shared: true,
			    useHTML: true,
			    yDecimals: 0,
			    valueDecimals: 0,
			    formatter: function() {
			    	//var points = '<span style="padding: 15px 0 5px 10px; display: block; font-size: 12px; line-height:13px;"><span></span>'+this.x+'</span><table>';
			    	var points = '<span style="padding: 15px 0 5px 10px; display: block; font-size: 12px; line-height:13px;"><span></span></span><table>';
			    	var totalCount = 0;
			    	$.each(this.points, function(i,point) {
			    		totalCount += point.y;
			    		points+='<tr><td style="padding:0 0 0 10px; display: block;font-size: 12px; margin:0; line-height:13px;">'+point.series.name+':</td><td style="margin:0;padding:0; line-height:13px;">'+point.y+'</td></tr>'
			    	});
			    	points+='<tr><td style="padding:0 0 0 10px; display: block;font-size: 12px; margin:0; line-height:13px;"><b>Total: </b></td>'
			    		+'<td style="text-align:right;font-size: 12px; margin:0; line-height:13px;"><b>'+totalCount+'</b></td></tr>'
			    		+'</table>';
 
			    	return points;
			    }
			},
			plotOptions : {
				column : {
					pointPadding : 0,
					borderWidth : 0
				}
			},
			series : [ {
				name : 'Passed',
				data : c
			}, {
				name : 'Failed',
				data : d
			}, {
				name : 'Skipped',
				data : e
			} ]
		});
	} else {
		$('#ci-chart-container').highcharts( {
			chart : {
				width : 780,
				height : 380,
				style: {
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				},
			},
			title : {
				text : 'No data available for this Quarter. <br/> To see the data - please select a different Quarter',
				align : 'left',
				x : 180,
				y : 180,
				floating : true
			},
			noData : {
				style : {
					fontWeight : 'bold',
					fontSize : '15px',
					color : '#303030',
					fontFamily:'"open_sansregular", "sans-serif","Arial"',
				}
			}
		});
	}
}
