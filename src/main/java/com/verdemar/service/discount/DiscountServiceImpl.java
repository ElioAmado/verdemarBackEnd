package com.verdemar.service.discount;

import com.verdemar.domain.discount.Discount;
import com.verdemar.domain.discount.DiscountRequestDto;
import com.verdemar.exception.DiscountNotFoundException;
import com.verdemar.repository.DiscountRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the DiscountService interface.
 */
@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {

    @Autowired 
    private DiscountRepository discountRepository;

    @Autowired
    private ModelMapper modelMapper;


    /**
     * Retrieves all discounts.
     *
     * @return a list of all Discount entities
     */
    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    /**
     * Retrieves a discount by its ID.
     *
     * @param id the ID of the discount
     * @return the Discount entity
     * @throws DiscountNotFoundException if the discount is not found
     */
    @Override
    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new DiscountNotFoundException(id));
    }

    /**
     * Creates a new discount.
     *
     * @param discount the Discount entity to create
     * @return the created Discount
     */
    @Override
    public Discount createDiscount(DiscountRequestDto discountDto) {
        Discount discount = modelMapper.map(discountDto, Discount.class);
        return discountRepository.save(discount);
    }

    /**
     * Updates an existing discount.
     *
     * @param id              the ID of the discount to update
     * @param updatedDiscount the new Discount data
     * @return the updated Discount
     * @throws DiscountNotFoundException if the discount is not found
     */
    @Override
    public Discount updateDiscount(Long id, Discount updatedDiscount) {
        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountNotFoundException(id));

        existingDiscount.setStartDate(updatedDiscount.getStartDate());
        existingDiscount.setEndDate(updatedDiscount.getEndDate());
        existingDiscount.setDiscount(updatedDiscount.getDiscount());
        existingDiscount.setIsPercentage(updatedDiscount.getIsPercentage());
        existingDiscount.setApartments(updatedDiscount.getApartments());

        return discountRepository.save(existingDiscount);
    }

    /**
     * Deletes a discount by its ID.
     *
     * @param id the ID of the discount to delete
     * @throws DiscountNotFoundException if the discount is not found
     */
    @Override
    public void deleteDiscount(Long id) {
        if (!discountRepository.existsById(id)) {
            throw new DiscountNotFoundException(id);
        }
        discountRepository.deleteById(id);
    }
}
