package com.verdemar.controller;

import com.verdemar.domain.discount.Discount;
import com.verdemar.domain.discount.DiscountRequestDto;
import com.verdemar.service.discount.DiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Discount entities.
 */
@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    /**
     * GET /api/discounts
     * Retrieves all discounts.
     */
    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        List<Discount> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discounts);
    }

    /**
     * GET /api/discounts/{id}
     * Retrieves a discount by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Discount> getDiscountById(@PathVariable Long id) {
        Discount discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(discount);
    }

    @GetMapping("/apartment/{apartmentId}")
    public ResponseEntity<List<Discount>> getDiscountsByApartmentId(@PathVariable Integer apartmentId) {
        List<Discount> discounts = discountService.getDiscountsByApartmentId(apartmentId);
        return ResponseEntity.ok(discounts);
    }

    /**
     * POST /api/discounts
     * Creates a new discount.
     */
    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody DiscountRequestDto discount) {
        Discount createdDiscount = discountService.createDiscount(discount);
        return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
    }

    /**
     * PUT /api/discounts/{id}
     * Updates an existing discount.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable Long id, @RequestBody Discount discount) {
        Discount updatedDiscount = discountService.updateDiscount(id, discount);
        return ResponseEntity.ok(updatedDiscount);
    }

    /**
     * DELETE /api/discounts/{id}
     * Deletes a discount by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
}
