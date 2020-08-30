/**
 * 
 */

$(document).ready(function(){
		fireAjaxCall( $('#year').val() );
	});

	$("#year").on('change', function(){
		fireAjaxCall( $('#year').val() );
	});

	function fireAjaxCall( year ) {
		$.ajax({
			url: 'multilinechart',
			method: 'POST',
			data: {year:year},
			success: function(result) {
				console.log(result);
				/* line chart multiple series starts here */
				var formatteddata = [];
				for(var key in result){
					var singleObject = {						
							name: '',
							data: []
						}
					singleObject.name = key.toUpperCase();
					for(var i = 0; i < result[key].length; i++){
						singleObject.data.push(parseInt(result[key][i].value));
					}
					formatteddata.push(singleObject);
				}
				
				console.log(formatteddata);
				drawMultipleLineChart(formatteddata);
			}
		});
	}


	/* for multiple line chart */
	function drawMultipleLineChart(formatteddata){
		Highcharts.chart('chart', {

		    title: {
		        text: 'Reports and Violations Statistics Chart'
		    },
		    
		    xAxis: {
		        categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		    },

		    yAxis: {
		        title: {
		            text: 'Number of reports/violations'
		        }
		    },
		    legend: {
		        layout: 'vertical',
		        align: 'right',
		        verticalAlign: 'middle'
		    },

		    plotOptions: {
		        series: {
		            label: {
		                connectorAllowed: false
		            },
		            pointStart: 0
		        }
		    },

		    series: formatteddata,

		    responsive: {
		        rules: [{
		            condition: {
		                maxWidth: 100,
						maxHeight: 100
		            },
		            chartOptions: {
		                legend: {
		                    layout: 'horizontal',
		                    align: 'center',
		                    verticalAlign: 'bottom'
		                }
		            }
		        }]
		    }

		});
	}


	function drawLineChart( month, values ) {
		Highcharts.chart('container', {
			chart: {
				type: 'line',
				width: 500
			},
			title : {
				text: 'Line Chart'
			},
			xAxis: {
				categories: month
			},
			tooltip: {
				formatter: function(){
					return '<strong>' + this.x + ': </strong>' + this.y;
				}
			},
			series: [{
				data: values
			}]
		});
	}