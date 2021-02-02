package com.firesale.api.mapper;

import com.firesale.api.dto.address.AddressDTO;
import com.firesale.api.dto.address.PatchAddressDTO;
import com.firesale.api.dto.address.UpdateAddressDTO;
import com.firesale.api.model.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {AddressMapperImpl.class})
class AddressMapperTests {
    @Autowired
    private AddressMapper addressMapper;

    @Test
    void testAddressMap() {
        // Setup mock repository
        Address address = this.get();

        var dto = addressMapper.toDTO(address);

        // Assert response
        Assertions.assertEquals(address.getId(), dto.getId(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getStreet(), dto.getStreet(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getCity(), dto.getCity(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getHouseNumber(), dto.getHouseNumber(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getPostalCode(), dto.getPostalCode(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getCountry(), dto.getCountry(), "The address returned was not the same as the mock");

    }

    @Test
    void testAddressDTOMap() {
        // Setup mock repository
        AddressDTO dto = this.getDTO();

        var address = addressMapper.toModel(dto);

        // Assert response
        Assertions.assertEquals(address.getId(), dto.getId(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getStreet(), dto.getStreet(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getCity(), dto.getCity(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getHouseNumber(), dto.getHouseNumber(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getPostalCode(), dto.getPostalCode(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getCountry(), dto.getCountry(), "The address returned was not the same as the mock");

    }

    @Test
    void testPatchAddressDTOMap() {
        // Setup mock repository
        PatchAddressDTO dto = this.getPatchDTO();

        var address = addressMapper.toModel(dto);

        // Assert response
        Assertions.assertEquals(address.getStreet(), dto.getStreet(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getCity(), dto.getCity(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getHouseNumber(), dto.getHouseNumber(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getPostalCode(), dto.getPostalCode(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getCountry(), dto.getCountry(), "The address returned was not the same as the mock");

    }

    @Test
    void testUpdateAddressDTOMap() {
        // Setup mock repository
        UpdateAddressDTO dto = this.getUpdateDTO();

        var address = addressMapper.toModel(dto);

        // Assert response
        Assertions.assertEquals(address.getStreet(), dto.getStreet(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getCity(), dto.getCity(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getHouseNumber(), dto.getHouseNumber(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getPostalCode(), dto.getPostalCode(), "The address returned was not the same as the mock");
        Assertions.assertEquals(address.getCountry(), dto.getCountry(), "The address returned was not the same as the mock");

    }


    private Address get()
    {
        Address address = new Address();
        address.setId(1L);
        address.setStreet("Achterwetering");
        address.setHouseNumber("23");
        address.setPostalCode("2871RK");
        address.setCity("Schoonhoven");
        address.setCountry("Nederland");
        return  address;
    }

    private AddressDTO getDTO()
    {
        AddressDTO address = new AddressDTO();
        address.setId(1L);
        address.setStreet("Achterwetering");
        address.setHouseNumber("23");
        address.setPostalCode("2871RK");
        address.setCity("Schoonhoven");
        address.setCountry("Nederland");
        return  address;
    }

    private PatchAddressDTO getPatchDTO()
    {
        PatchAddressDTO address = new PatchAddressDTO();
        address.setStreet("Achterwetering");
        address.setHouseNumber("23");
        address.setPostalCode("2871RK");
        address.setCity("Schoonhoven");
        address.setCountry("Nederland");
        return  address;
    }

    private UpdateAddressDTO getUpdateDTO()
    {
        UpdateAddressDTO address = new UpdateAddressDTO();
        address.setStreet("Achterwetering");
        address.setHouseNumber("23");
        address.setPostalCode("2871RK");
        address.setCity("Schoonhoven");
        address.setCountry("Nederland");
        return  address;
    }
}
