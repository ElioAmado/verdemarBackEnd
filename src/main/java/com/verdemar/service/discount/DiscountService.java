package com.verdemar.service.discount;

import java.util.List;

import com.verdemar.domain.discount.Discount;
import com.verdemar.domain.discount.DiscountRequestDto;

/**
 * Service interface for managing Discount entities.
 */
public interface DiscountService {

    /**
     * Retrieves all discounts.
     *
     * @return a list of all Discount entities
     */
    List<Discount> getAllDiscounts();

    /**
     * Retrieves a discount by its ID.
     *
     * @param id the ID of the discount
     * @return the Discount entity
     */
    Discount getDiscountById(Long id);

    List<Discount> getDiscountsByApartmentId(Integer apartmentId);

    /**
     * Creates a new discount.
     *
     * @param discount the Discount entity to create
     * @return the created Discount
     */
    Discount createDiscount(DiscountRequestDto discount);

    /**
     * Updates an existing discount.
     *
     * @param id       the ID of the discount to update
     * @param discount the new Discount data
     * @return the updated Discount
     */
    Discount updateDiscount(Long id, Discount discount);

    /**
     * Deletes a discount by its ID.
     *
     * @param id the ID of the discount to delete
     */
    void deleteDiscount(Long id);
}
