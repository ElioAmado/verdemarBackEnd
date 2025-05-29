package com.verdemar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verdemar.domain.Child;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByBookingId(Long bookingId);
    
}
