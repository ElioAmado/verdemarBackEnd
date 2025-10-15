package com.verdemar.service.bed;

import com.verdemar.domain.Bed;
import java.util.List;

public interface BedService {

  List<Bed> getAllBeds();

  Bed getBedById(Short id);

  Bed createBed(Bed bed);

  Bed updateBed(Short id, Bed bed);

  void deleteBed(Short id);
}
