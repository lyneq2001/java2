package com.library.controller;

import com.library.entity.User;
import com.library.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class NotificationAdvice {

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addNotifications(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("notifications", notificationService.getNotifications(user));
            model.addAttribute("unreadCount", notificationService.countUnread(user));
        }
    }
}
