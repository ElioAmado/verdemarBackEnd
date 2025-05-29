package com.verdemar.service;

import java.util.List;
import java.util.Optional;

import com.verdemar.domain.Child;

public interface ChildService {

    public List<Child> getAllChildren();

    public Optional<Child> getChildById(Long id);


    public List<Child> getChildrenByBookingId(Long bookingId);

    public Child createChild(Long bookingId, byte age);

    public Child updateChild(Long childId, byte newAge);
    public void deleteChild(Long childId) ;
}
