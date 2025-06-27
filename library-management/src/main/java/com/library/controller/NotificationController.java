package com.library.controller;

import com.library.entity.Notification;
import com.library.entity.User;
import com.library.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications/{id}/open")
    public String openNotification(@PathVariable Long id, Authentication authentication) {
        String redirect = "/";
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            for (Notification n : notificationService.getUnreadNotifications(user)) {
                if (n.getId().equals(id)) {
                    notificationService.markAsRead(id);
                    notificationService.deleteNotification(id);
                    if (n.getLink() != null && !n.getLink().isBlank()) {
                        redirect = n.getLink();
                    }
                    break;
                }
            }
        }
        return "redirect:" + redirect;
    }
}
