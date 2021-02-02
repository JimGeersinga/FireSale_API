package com.firesale.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.firesale.api.dto.address.AddressDTO;
import com.firesale.api.dto.address.PatchAddressDTO;
import com.firesale.api.dto.address.UpdateAddressDTO;
import com.firesale.api.dto.auction.AuctionDTO;
import com.firesale.api.dto.auction.CreateImageDTO;
import com.firesale.api.dto.user.*;
import com.firesale.api.dto.usersecurity.ChangepasswordDTO;
import com.firesale.api.dto.usersecurity.EmailaddressDTO;
import com.firesale.api.exception.GlobalExceptionHandler;
import com.firesale.api.mapper.AddressMapper;
import com.firesale.api.mapper.AuctionMapper;
import com.firesale.api.mapper.UserMapper;
import com.firesale.api.model.Address;
import com.firesale.api.model.Auction;
import com.firesale.api.model.Gender;
import com.firesale.api.model.User;
import com.firesale.api.security.UserPrincipal;
import com.firesale.api.service.*;
import com.firesale.api.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {
    private MockMvc mvc;
    @Mock
    private UserService userService;
    @Mock
    private AddressService addressService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private ImageService imageService;
    @Mock
    private AuctionService auctionService;
    @Mock
    private AuctionMapper auctionMapper;
    @Mock
    private UserSecurityService userSecurityService;

    @InjectMocks
    private UserController userController;

    private JacksonTester<LoginDTO> loginDTOJacksonTester;

    private JacksonTester<RegisterDTO> registerDTOJacksonTester;
    private JacksonTester<UpdateUserDTO> updateUserDTOJacksonTester;
    private JacksonTester<PatchUserDTO> PatchUserDTOJacksonTester;
    private JacksonTester<UpdateAddressDTO> updateAddressDTOJacksonTester;
    private JacksonTester<PatchAddressDTO> patchAddressDTOJacksonTester;
    private JacksonTester<CreateImageDTO> createImageDTOJacksonTester;
    private JacksonTester<EmailaddressDTO> emailaddressDTOJacksonTester;
    private JacksonTester<ChangepasswordDTO> changepasswordDTOJacksonTester;
    private JacksonTester<PatchUserDTO> patchUserDtoJacksonTester;

    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void authenticate() throws Exception {
        // given
        LoginDTO c = new LoginDTO();
        c.setEmail("test@firesale.com");
        c.setPassword("test@welkom123");

        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);

        doReturn(user).when(userService).authenticate(anyString(), anyString());

        when(userMapper.toDTO(any(User.class))).thenAnswer((i)->{
            var u = (User)i.getArguments()[0];
            var dto = new UserDTO();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/users/authenticate").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(loginDTOJacksonTester.write(c).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("test");
    }

    @Test
    void authenticateFailedByLock() throws Exception {
        // given
        LoginDTO c = new LoginDTO();
        c.setEmail("test@firesale.com");
        c.setPassword("test@welkom123");

        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(true);

        doReturn(user).when(userService).authenticate(anyString(), anyString());


        // when
        MockHttpServletResponse response = mvc.perform(
                post("/users/authenticate").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(loginDTOJacksonTester.write(c).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).contains("Account is locked.");
    }

    @Test
    void authenticateFailedByNull() throws Exception {
        // given
        LoginDTO c = new LoginDTO();
        c.setEmail("test@firesale.com");
        c.setPassword("test@welkom123");

        doReturn(null).when(userService).authenticate(anyString(), anyString());


        // when
        MockHttpServletResponse response = mvc.perform(
                post("/users/authenticate").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(loginDTOJacksonTester.write(c).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).contains("Email or password is incorrect.");
    }

    @Test
    void postDTO() throws Exception {
        // given
        RegisterDTO c = new RegisterDTO();
        c.setEmail("test@firesale.com");
        c.setFirstName("test@firesale.com");
        c.setLastName("test@firesale.com");
        c.setDisplayName("test");
        c.setGender(Gender.OTHER);
        c.setDateOfBirth(null);
        c.setPassword("testWelkom123#");

        UpdateAddressDTO addressDTO = new UpdateAddressDTO();
        addressDTO.setCity("stad");
        addressDTO.setCountry("Nederland");
        addressDTO.setHouseNumber("10");
        addressDTO.setPostalCode("2871RK");
        c.setAddress(addressDTO);

        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);

        doReturn(user).when(userService).create(any(User.class));

        when(userMapper.toModel(any(RegisterDTO.class))).thenAnswer((i)->{
            var u = (RegisterDTO)i.getArguments()[0];
            var dto = new User();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });

        when(userMapper.toDTO(any(User.class))).thenAnswer((i)->{
            var u = (User)i.getArguments()[0];
            var dto = new UserDTO();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });

        var json = registerDTOJacksonTester.write(c).getJson();
        json = json.replace("\"dateOfBirth\":null", "\"dateOfBirth\":\"1992-07-12\"");
        // when
        MockHttpServletResponse response = mvc.perform(
                post("/users").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).contains("test");
    }

    @Test
    void updateUser() throws Exception {
        // given
        UpdateUserDTO c = new UpdateUserDTO();
        c.setEmail("test@firesale.com");
        c.setFirstName("test@firesale.com");
        c.setLastName("test@firesale.com");
        c.setDisplayName("test");
        c.setGender(Gender.OTHER);
        c.setDateOfBirth(null);



        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);


        when(userMapper.toModel(any(UpdateUserDTO.class))).thenAnswer((i)->{
            var u = (UpdateUserDTO)i.getArguments()[0];
            var dto = new User();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });
        var json = updateUserDTOJacksonTester.write(c).getJson();
        json = json.replace("\"dateOfBirth\":null", "\"dateOfBirth\":\"1992-07-12\"");
        // when
        MockHttpServletResponse response = mvc.perform(
                put("/users/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void patchUser() throws Exception {
        // given
        PatchUserDTO c = new PatchUserDTO();
        c.setEmail("test@firesale.com");
        c.setFirstName("test@firesale.com");
        c.setLastName("test@firesale.com");
        c.setDisplayName("test");
        c.setGender(Gender.OTHER);
        c.setDateOfBirth(null);
        CreateImageDTO avatar = new CreateImageDTO();
        avatar.setPath(new byte[5]);
        c.setAvatar(avatar);

        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);


        when(userMapper.toModel(any(PatchUserDTO.class))).thenAnswer((i)->{
            var u = (PatchUserDTO)i.getArguments()[0];
            var dto = new User();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });

        doNothing().when(imageService).storeAvatar(any(CreateImageDTO.class), anyLong());
        doReturn(user).when(userService).patchUser(anyLong(),any(User.class));

        var json = patchUserDtoJacksonTester.write(c).getJson();
        json = json.replace("\"dateOfBirth\":null", "\"dateOfBirth\":\"1992-07-12\"");
        // when
        MockHttpServletResponse response = mvc.perform(
                patch("/users/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void patchUserNoAvatar() throws Exception {
        // given
        PatchUserDTO c = new PatchUserDTO();
        c.setEmail("test@firesale.com");
        c.setFirstName("test@firesale.com");
        c.setLastName("test@firesale.com");
        c.setDisplayName("test");
        c.setGender(Gender.OTHER);
        c.setDateOfBirth(null);

        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);


        when(userMapper.toModel(any(PatchUserDTO.class))).thenAnswer((i)->{
            var u = (PatchUserDTO)i.getArguments()[0];
            var dto = new User();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });

        doReturn(user).when(userService).patchUser(anyLong(),any(User.class));

        var json = patchUserDtoJacksonTester.write(c).getJson();
        json = json.replace("\"dateOfBirth\":null", "\"dateOfBirth\":\"1992-07-12\"");
        // when
        MockHttpServletResponse response = mvc.perform(
                patch("/users/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void patchUserWrongAvatar() throws Exception {
        // given
        PatchUserDTO c = new PatchUserDTO();
        c.setEmail("test@firesale.com");
        c.setFirstName("test@firesale.com");
        c.setLastName("test@firesale.com");
        c.setDisplayName("test");
        c.setGender(Gender.OTHER);
        c.setDateOfBirth(null);
        CreateImageDTO avatar = new CreateImageDTO();
        avatar.setPath(new byte[0]);
        c.setAvatar(avatar);

        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);


        when(userMapper.toModel(any(PatchUserDTO.class))).thenAnswer((i)->{
            var u = (PatchUserDTO)i.getArguments()[0];
            var dto = new User();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });

        doReturn(user).when(userService).patchUser(anyLong(),any(User.class));

        var json = patchUserDtoJacksonTester.write(c).getJson();
        json = json.replace("\"dateOfBirth\":null", "\"dateOfBirth\":\"1992-07-12\"");
        // when
        MockHttpServletResponse response = mvc.perform(
                patch("/users/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getMe() throws Exception {
//         given
        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);
        UserPrincipal principal = new UserPrincipal(user);
        when(userMapper.toDTO(any(User.class))).thenAnswer((i)->{
            var u = (User)i.getArguments()[0];
            var dto = new UserDTO();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });
        try(MockedStatic<SecurityUtil> mockSecUtil = Mockito.mockStatic(SecurityUtil.class)){
            mockSecUtil.when(SecurityUtil::getSecurityContextUser).thenAnswer((i)->principal);
            // when
            MockHttpServletResponse response = mvc.perform(
                    get("/users/me")
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();
            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).contains("test");
        }
    }

    @Test
    void getAll() throws Exception {
//         given
        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);
        when(userMapper.toProfile(any(User.class))).thenAnswer((i)->{
            var u = (User)i.getArguments()[0];
            var dto = new UserProfileDTO();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });
        doReturn(Collections.singletonList(user)).when(userService).getAll();

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("test");

    }

    @Test
    void deleteUser() throws Exception {
//         given
        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);
        UserPrincipal principal = new UserPrincipal(user);

        try(MockedStatic<SecurityUtil> mockSecUtil = Mockito.mockStatic(SecurityUtil.class)){
            mockSecUtil.when(SecurityUtil::getSecurityContextUser).thenAnswer((i)->principal);
            doNothing().when(userService).delete(any(User.class));
            // when
            MockHttpServletResponse response = mvc.perform(
                    delete("/users")
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();
            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }


    @Test
    void getSingle() throws Exception {
//         given
        User user = new User();
        user.setDisplayName("test");
        user.setIsLocked(false);
        when(userMapper.toProfile(any(User.class))).thenAnswer((i)->{
            var u = (User)i.getArguments()[0];
            var dto = new UserProfileDTO();
            dto.setDisplayName(u.getDisplayName());
            return dto;
        });
        doReturn(user).when(userService).findUserById(anyLong());

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("test");

    }

    @Test
    void changePassword() throws Exception {
//         given
        ChangepasswordDTO user = new ChangepasswordDTO();
        user.setPassword("test");
        user.setToken("false");

        doNothing().when(userSecurityService).changeUserPassword(anyString(), anyString());

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/users/changepassword").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(changepasswordDTOJacksonTester.write(user).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void resetPassword() throws Exception {
//         given
        EmailaddressDTO user = new EmailaddressDTO();
        user.setEmailaddress("test");

        doNothing().when(userSecurityService).createPasswordResetTokenForUser(anyString());

        // when
        MockHttpServletResponse response = mvc.perform(
                post("/users/forgotpassword").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(emailaddressDTOJacksonTester.write(user).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void getAuctionsByUserId() throws Exception {
//         given
        Auction auction = new Auction();
        auction.setName("test");

        doReturn(Collections.singletonList(auction)).when(auctionService).getAuctionsByUserId(anyLong());

        when(auctionMapper.toDTO(any(Auction.class))).thenAnswer((i)->{
            var u = (Auction)i.getArguments()[0];
            var dto = new AuctionDTO();
            dto.setName(u.getName());
            return dto;
        });

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/users/1/auctions").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void uploadAvatar() throws Exception {
//         given
        CreateImageDTO auction = new CreateImageDTO();
        auction.setPath(new byte[0]);
        auction.setType("test");

        doNothing().when(imageService).storeAvatar(any(CreateImageDTO.class), anyLong());


        // when
        MockHttpServletResponse response = mvc.perform(
                post("/users/1/avatar").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(createImageDTOJacksonTester.write(auction).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void updateAddress() throws Exception {
//         given
        UpdateAddressDTO auction = new UpdateAddressDTO();
        auction.setStreet("kerkstraat");
        auction.setHouseNumber("1");
        auction.setPostalCode("1111AA");
        auction.setCity("Amsterdam");
        auction.setCountry("Nederland");

        doReturn(new Address()).when(addressService).updateAddress( anyLong(),any(Address.class));
        when(addressMapper.toModel(any(UpdateAddressDTO.class))).thenAnswer((i)->{
            var u = (UpdateAddressDTO)i.getArguments()[0];
            var dto = new Address();
            dto.setPostalCode(u.getPostalCode());
            return dto;
        });

        // when
        MockHttpServletResponse response = mvc.perform(
                put("/users/1/address/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(updateAddressDTOJacksonTester.write(auction).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void patchAddress() throws Exception {
//         given
        PatchAddressDTO auction = new PatchAddressDTO();
        auction.setPostalCode("1111AA");

        doReturn(new Address()).when(addressService).patchAddress( anyLong(),any(Address.class));
        when(addressMapper.toModel(any(PatchAddressDTO.class))).thenAnswer((i)->{
            var u = (PatchAddressDTO)i.getArguments()[0];
            var dto = new Address();
            dto.setPostalCode(u.getPostalCode());
            return dto;
        });


        // when
        MockHttpServletResponse response = mvc.perform(
                patch("/users/1/address/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(patchAddressDTOJacksonTester.write(auction).getJson()))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getAddress() throws Exception {
//         given
        PatchAddressDTO auction = new PatchAddressDTO();
        auction.setPostalCode("1111AA");

        doReturn(new Address()).when(addressService).findAddressById( anyLong());
        when(addressMapper.toDTO(any(Address.class))).thenAnswer((i)->{
            var u = (Address)i.getArguments()[0];
            var dto = new AddressDTO();
            dto.setPostalCode(u.getPostalCode());
            return dto;
        });
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/users/1/address/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }



}
