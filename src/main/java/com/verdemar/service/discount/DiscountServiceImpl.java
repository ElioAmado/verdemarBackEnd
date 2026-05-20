package com.verdemar.service.discount;

import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.discount.Discount;
import com.verdemar.domain.discount.DiscountRequestDto;
import com.verdemar.exception.DiscountNotFoundException;
import com.verdemar.repository.DiscountRepository;
import com.verdemar.service.apartment.ApartmentService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Implementation of the DiscountService interface.
 */
@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {

    @Autowired 
    private DiscountRepository discountRepository;

    @Autowired
    private ApartmentService apartmentService;

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
    public Discount createDiscount(DiscountRequestDto discountDto) { //Tiene que comprobat que esten disponibles els apartaments
        
        Set<Apartment> apartments = new java.util.HashSet<>();
        for (Integer apartmentId : discountDto.getApartmentIds()) {
            apartments.add(apartmentService.getApartmentById(apartmentId));
            
        }
        
        Discount discount = modelMapper.map(discountDto, Discount.class);
        discount.setApartments(apartments);
        return discountRepository.save(discount);
    }

    @Override
    public List<Discount> getDiscountsByApartmentId(Integer apartmentId) {
        return discountRepository.findByApartmentsId(apartmentId);
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
    public Discount updateDiscount(Long id, Discount updatedDiscount) { //Tiene que comprobat que esten disponibles els apartaments
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
