package com.verdemar.verdemar.service;

import com.verdemar.verdemar.domain.Bed;
import java.util.List;

public interface BedService {

    List<Bed> getAllBeds();

    Bed getBedById(Short id);

    Bed createBed(Bed bed);

    Bed updateBed(Short id, Bed bed);

    void deleteBed(Short id);
}
