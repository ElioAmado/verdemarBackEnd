package com.verdemar.service.bed;

import com.verdemar.domain.Bed;
import com.verdemar.repository.BedRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BedServiceImpl implements BedService {

  @Autowired private BedRepository bedRepository;

  // Devuelve todos los camas
  @Override
  public List<Bed> getAllBeds() {
    return bedRepository.findAll();
  }

  // Devuelve una cama por su ID
  @Override
  public Bed getBedById(Short id) {
    Optional<Bed> bed = bedRepository.findById(id);
    return bed.orElseThrow(() -> new RuntimeException("Bed not found with id: " + id));
  }

  // Crea una nueva cama
  @Override
  public Bed createBed(Bed bed) {
    return bedRepository.save(bed);
  }

  @Override
  public Bed updateBed(Short id, Bed bed) {
    if (!bedRepository.existsById(id)) {
      throw new RuntimeException("Bed not found with id: " + id);
    }
    bed.setId(id); // Asegura que se mantiene el mismo ID
    return bedRepository.save(bed);
  }

  @Override
  public void deleteBed(Short id) {
    if (!bedRepository.existsById(id)) {
      throw new RuntimeException("Bed not found with id: " + id);
    }
    bedRepository.deleteById(id);
  }
}
