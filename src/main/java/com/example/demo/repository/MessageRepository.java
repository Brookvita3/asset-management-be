package com.example.demo.repository;

import com.example.demo.entity.chat_bot.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.user.id = :userId ORDER BY m.createdAt DESC")
    List<Message> findRecentMessagesByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM Message m WHERE m.user.id = :userId ORDER BY m.createdAt DESC LIMIT 10")
    List<Message> findTop10ByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}