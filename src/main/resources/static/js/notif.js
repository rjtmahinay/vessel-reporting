/**
 * 
 */

	$(document).ready(function(){
 
				function load_unseen_notification(view = '')
				{
					$.ajax({
						url:"/fetch-report-records",
						method:"POST",
						data:{view:view},
						success:function(data)
						{
							// console.log("data: " + data);

							console.log(data);

							var result = data;
							console.log("notification: " + result.notifications);
							 console.log('unseen: ' + result.unseen_notifications);
				
							
							if(parseInt(result.unseen_notifications) > 0)
							{
								$('.count').html(result.unseen_notifications);
								$('#notification').html(result.notifications);
							} 
						},
						error:function(req, status, error)
						{
							console.log("req: " + req);
							console.log("status: " + status);
							console.log("error: " + error);
						}
					});
				}

				load_unseen_notification();

				 $(document).on('click', '#notif', function(){
					$('.count').html('');
					load_unseen_notification('yes');
				}); 
 
				 setInterval(function(){ 
					load_unseen_notification();
				}, 5000);  
				
			});