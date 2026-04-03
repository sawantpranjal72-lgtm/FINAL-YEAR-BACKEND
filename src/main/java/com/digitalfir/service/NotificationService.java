package com.digitalfir.service;

import com.digitalfir.backend.model.Notification;
import com.digitalfir.backend.model.NotificationType; // FIX
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // 1️⃣ Create notification
    public void createNotification(
            User user,
            String message,
            NotificationType type,
            Long referenceId) {

        Notification n = new Notification();
        n.setUser(user);
        n.setMessage(message);
        n.setType(type);
        n.setReferenceId(referenceId);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(n);
    }

    // 2️⃣ Get notifications
    public List<Notification> getMyNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // 3️⃣ Unread count
    public long getUnreadCount(User user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }

    // 4️⃣ Mark as read
    public void markAsRead(Long notificationId, User user) {

        Notification n = notificationRepository
                .findByIdAndUser(notificationId, user)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (n.isRead()) return;

        n.setRead(true);
        notificationRepository.save(n);
    }
}