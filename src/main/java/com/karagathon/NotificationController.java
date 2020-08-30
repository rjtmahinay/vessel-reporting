package com.karagathon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.karagathon.model.Notification;
import com.karagathon.service.NotificationService;

@Controller
public class NotificationController {

	@Autowired
	NotificationService notificationService;

	@ModelAttribute("loggedInUser")
	public void userAttribute(Model model) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		model.addAttribute("loggedInUser", name);

	}

	@PostMapping("/fetch-report-records")
	public ResponseEntity<?> getNotificationsViaAjax(@Valid @RequestParam("view") String search) {

//        AjaxResponseBody result = new AjaxResponseBody();

		String result;
		int count = 0;
		List<Notification> notifications = notificationService.getNotifications(5);

		Map<String, String> notificationData = new HashMap<>();

		System.out.println(notifications);

		final StringBuffer output = new StringBuffer();
		if (Objects.isNull(search) || search.trim().isEmpty()) {
			if (!notifications.isEmpty()) {
				count = notifications.size();
				notifications.forEach(notification -> {
					output.append("<li ><a class='btn btn-link' style='background: #fff; ' href='")
							.append(notification.getUrl()) // . $row->subject . . $row->text .
							.append("'><strong>").append(notification.getSubject()).append("</strong>")
							.append("</a></li><li class='dropdown-divider'></li>");
				});

			}

			else {
				output.append(
						"<li><a href=\"#\" class=\"text-bold text-italic\"  style='background: #fff; '>No Notifications Found</a></li>");
			}
		} else {
			notificationService.updateUnseenNotifications();
		}

		System.out.println("output: " + output);
		notificationData.put("unseen_notifications", Integer.toString(count));
		notificationData.put("notifications", output.toString());

		return ResponseEntity.ok(notificationData);

	}
}
