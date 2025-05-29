package com.verdemar.controller;

import com.verdemar.domain.Child;
import com.verdemar.service.ChildService;
import com.verdemar.service.ChildServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildServiceImpl childService) {
        this.childService = childService;
    }

    // GET /api/children
    @GetMapping
    public List<Child> getAllChildren() {
        return childService.getAllChildren();
    }

    // GET /api/children/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id) {
        return childService.getChildById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/children/booking/{bookingId}
    @GetMapping("/booking/{bookingId}")
    public List<Child> getChildrenByBooking(@PathVariable Long bookingId) {
        return childService.getChildrenByBookingId(bookingId);
    }

    // POST /api/children
    @PostMapping
    public ResponseEntity<Child> createChild(
            @RequestParam Long bookingId,
            @RequestParam byte age) {
        Child created = childService.createChild(bookingId, age);
        return ResponseEntity.ok(created);
    }

    // PUT /api/children/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(
            @PathVariable Long id,
            @RequestParam byte age) {
        try {
            Child updated = childService.updateChild(id, age);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/children/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        try {
            childService.deleteChild(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
