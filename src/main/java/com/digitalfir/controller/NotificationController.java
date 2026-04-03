package com.digitalfir.controller;

import com.digitalfir.backend.model.Notification;
import com.digitalfir.backend.model.User;
import com.digitalfir.service.NotificationService;
import com.digitalfir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    // 1️⃣ Get all notifications
    @GetMapping
    public List<Notification> getMyNotifications(Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        return notificationService.getMyNotifications(user);
    }

    // 2️⃣ 🔔 Unread count
    @GetMapping("/unread-count")
    public long getUnreadCount(Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        return notificationService.getUnreadCount(user);
    }

    // 3️⃣ Mark as read
    @PutMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        notificationService.markAsRead(id, user);
        return "Notification marked as read";
    }
}