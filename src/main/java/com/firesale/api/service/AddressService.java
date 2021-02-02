package com.firesale.api.service;

import com.firesale.api.exception.ResourceNotFoundException;
import com.firesale.api.model.Address;
import com.firesale.api.model.ErrorTypes;
import com.firesale.api.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;

    public Address findAddressById(final long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No address exists for id: %d", addressId), ErrorTypes.ADDRESS_NOT_FOUND));
    }

    @Transactional(readOnly = false)
    public Address updateAddress(Long addressId, Address address) {
        final Address existing = findAddressById(addressId);
        existing.setStreet(address.getStreet());
        existing.setHouseNumber(address.getHouseNumber());
        existing.setPostalCode(address.getPostalCode());
        existing.setCity(address.getCity());
        existing.setCountry(address.getCountry());
        return addressRepository.save(existing);
    }

    @Transactional(readOnly = false)
    public Address patchAddress(Long addressId, Address address) {
        final Address existing = findAddressById(addressId);
        if (!address.getStreet().isEmpty())
            existing.setStreet(address.getStreet());
        if (!address.getHouseNumber().isEmpty())
            existing.setHouseNumber(address.getHouseNumber());
        if (!address.getPostalCode().isEmpty())
            existing.setPostalCode(address.getPostalCode());
        if (!address.getCity().isEmpty())
            existing.setCity(address.getCity());
        if (!address.getCountry().isEmpty())
            existing.setCountry(address.getCountry());
        return addressRepository.save(existing);
    }
}
