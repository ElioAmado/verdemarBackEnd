package com.verdemar.controller;

import com.verdemar.domain.Bed;
import com.verdemar.service.bed.BedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
public class BedController {

    @Autowired
    private BedService bedService;

    // Obtener todos los camas
    @GetMapping
    public ResponseEntity<List<Bed>> getAllBeds() {
        List<Bed> beds = bedService.getAllBeds();
        return ResponseEntity.ok(beds);
    }

    // Obtener una cama por ID
    @GetMapping("/{id}")
    public ResponseEntity<Bed> getBedById(@PathVariable Short id) {
        Bed bed = bedService.getBedById(id);
        return ResponseEntity.ok(bed);
    }

    // Crear una nueva cama
    @PostMapping
    public ResponseEntity<Bed> createBed(@RequestBody Bed bed) {
        Bed createdBed = bedService.createBed(bed);
        return ResponseEntity.ok(createdBed);
    }

    // Actualizar una cama
    @PutMapping("/{id}")
    public ResponseEntity<Bed> updateBed(@PathVariable Short id, @RequestBody Bed bed) {
        Bed updatedBed = bedService.updateBed(id, bed);
        return ResponseEntity.ok(updatedBed);
    }

    // Eliminar una cama
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBed(@PathVariable Short id) {
        bedService.deleteBed(id);
        return ResponseEntity.noContent().build();
    }
}
