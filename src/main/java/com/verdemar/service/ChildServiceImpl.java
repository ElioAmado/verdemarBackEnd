package com.verdemar.service;

import com.verdemar.domain.Booking;
import com.verdemar.domain.Child;
import com.verdemar.repository.BookingRepository;
import com.verdemar.repository.ChildRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final BookingRepository bookingRepository;

    public ChildServiceImpl(ChildRepository childRepository, BookingRepository bookingRepository) {
        this.childRepository = childRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }

    public Optional<Child> getChildById(Long id) {
        return childRepository.findById(id);
    }

    public List<Child> getChildrenByBookingId(Long bookingId) {
        return childRepository.findByBookingId(bookingId);
    }

    public Child createChild(Long bookingId, byte age) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Child child = new Child();
        child.setAge(age);
        child.setBooking(booking);
        return childRepository.save(child);
    }

    public Child updateChild(Long childId, byte newAge) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("Child not found"));

        child.setAge(newAge);
        return childRepository.save(child);
    }

    public void deleteChild(Long childId) {
        if (!childRepository.existsById(childId)) {
            throw new IllegalArgumentException("Child not found");
        }
        childRepository.deleteById(childId);
    }
}
