package com.firesale.api.service;

import com.firesale.api.exception.ResourceNotFoundException;
import com.firesale.api.exception.UnAuthorizedException;
import com.firesale.api.exception.UserRegistrationException;
import com.firesale.api.model.*;
import com.firesale.api.repository.AddressRepository;
import com.firesale.api.repository.AuctionRepository;
import com.firesale.api.repository.BidRepository;
import com.firesale.api.repository.UserRepository;
import com.firesale.api.security.Guard;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class  UserServiceTests {
    @Mock
    UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

    @Mock
    AuctionRepository auctionRepository;

    @Mock
    BidRepository bidRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    private static MockedStatic<Guard> mockedGuard;

    @BeforeAll
    public static void init() {
        mockedGuard = mockStatic(Guard.class);
    }

    @AfterAll
    public static void close() {
        mockedGuard.close();
    }


    @Test
    void shouldFindUserByEmailSuccessfully(){
        final User givenUser = getUser();
        givenUser.setEmail("admin@firesale.nl");

        when(userRepository.findByEmail(eq("admin@firesale.nl"))).thenReturn(Optional.of(givenUser));

        final User user = userService.findUserByEmail("admin@firesale.nl");
        assertThat(user.getEmail()).isEqualTo(givenUser.getEmail());

        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void shouldThrowErrorWhenFindingUserWithNonExistingEmail(){
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.findUserByEmail("admin@firesale.nl"));

        assertThat(exception.getMessage()).isEqualTo("Resource was not found: [No user exists for email: admin@firesale.nl]");

        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void shouldFindUserByIdSuccessfully(){
        final User givenUser = getUser();
        givenUser.setId(5L);

        when(userRepository.findById(eq(5L))).thenReturn(Optional.of(givenUser));

        final User user = userService.findUserById(5L);
        assertThat(user.getId()).isEqualTo(givenUser.getId());

        verify(userRepository).findById(any(Long.class));
    }

    @Test
    void getAll(){
        var givenUser = Collections.singletonList(getUser());

        doReturn(givenUser).when(userRepository).findAll();

        var users = userService.getAll();
        assertSame(givenUser, users, "Not the provided mocks");

        verify(userRepository).findAll();
    }

    @Test
    void shouldThrowErrorWhenFindingUserWithNonExistingId(){
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(5L));

        assertThat(exception.getMessage()).isEqualTo("Resource was not found: [No user exists for id: 5]");

        verify(userRepository).findById(any(Long.class));
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        final User givenUser = new User();
        givenUser.setEmail("admin@firesale.nl");
        givenUser.setPassword("$2a$10$zO.P/jNe8LedLkSAD67AIOXOktIBbsncYE7VcP/cUxvgkiZ4dBgRi");

        when(userRepository.findByEmail(eq("admin@firesale.nl"))).thenReturn(Optional.of(givenUser));
        when(passwordEncoder.matches(eq("admin"), eq(givenUser.getPassword()))).thenReturn(true);

        final User user = userService.authenticate("admin@firesale.nl", "admin");
        assertThat(user).isNotNull();

        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void shouldNotAuthenticateUserSuccessfully() {
        final User givenUser = new User();
        givenUser.setEmail("admin@firesale.nl");
        givenUser.setPassword("$2a$10$zO.P/jNe8LedLkSAD67AIOXOktIBbsncYE7VcP/cUxvgkiZ4dBgRi");

        when(userRepository.findByEmail(eq("admin@firesale.nl"))).thenReturn(Optional.of(givenUser));
        when(passwordEncoder.matches(eq("abcd"), eq(givenUser.getPassword()))).thenReturn(false);

        final User user = userService.authenticate("admin@firesale.nl", "abcd");
        assertThat(user).isNull();

        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void shouldNotAuthenticateUserEmptySuccessfully() {
        final User givenUser = new User();
        givenUser.setEmail("admin@firesale.nl");
        givenUser.setPassword("$2a$10$zO.P/jNe8LedLkSAD67AIOXOktIBbsncYE7VcP/cUxvgkiZ4dBgRi");

        when(userRepository.findByEmail(eq("admin@firesale.nl"))).thenReturn(Optional.empty());

        final User user = userService.authenticate("admin@firesale.nl", "abcd");
        assertThat(user).isNull();

        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void shouldCreateUserSuccessfully() {
        final User user = getUser();
        user.setId(null);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.create(user);

        assertThat(savedUser).isNotNull();

        verify(userRepository).findByEmail(any(String.class));
        verify(userRepository).save(any(User.class));
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void shouldCreateUserWithSASuccessfully() {
        final User user = getUser();
        user.setId(null);
        user.setShippingAddress(new Address());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.create(user);

        assertThat(savedUser).isNotNull();

        verify(userRepository).findByEmail(any(String.class));
        verify(userRepository).save(any(User.class));
        verify(addressRepository, times(2)).save(any(Address.class));
    }

    @Test
    void shouldThrowErrorWhenCreatingUserWithExistingEmail() {
        final User user = getUser();
        user.setId(null);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserRegistrationException.class, () -> userService.create(user));

        verify(userRepository).findByEmail(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldUpdateUserSuccessfully(){
        final User user = getUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        final User expected = userService.updateUser(user.getId(), user);

        assertThat(expected).isNotNull();

        verify(userRepository).findById(any(Long.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void adminShouldPatchUserSuccessfully() {
        final User user = new User();
        user.setIsLocked(true);
        user.setRole(Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        mockedGuard.when(Guard::isAdmin).thenReturn(true);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);

        User patchedUser = userService.patchUser(1L, user);

        assertThat(patchedUser).isEqualTo(user);

        verify(userRepository).findById(any(Long.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void adminShouldThrowErrorWhenPatchingUser() {
        final User user = new User();
        user.setIsLocked(true);
        user.setRole(Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockedGuard.when(Guard::isAdmin).thenReturn(true);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(true);

        Exception exception = assertThrows(UnAuthorizedException.class, () -> userService.patchUser(1L, user));

        assertThat(exception.getMessage()).isEqualTo("User is not authorized for: [Cannot patch admin fields]");

        verify(userRepository).findById(any(Long.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void userShouldPatchUserSuccessfully() {
        final User user = new User();
        user.setEmail("john@firesale.nl");
        user.setDisplayName("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.now());
        user.setPassword("test");

        doReturn("TEST").when(passwordEncoder).encode(anyString());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        mockedGuard.when(Guard::isAdmin).thenReturn(false);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(true);

        User patchedUser = userService.patchUser(1L, user);

        assertThat(patchedUser).isEqualTo(user);

        verify(userRepository).findById(any(Long.class));
        verify(userRepository).save(any(User.class));
    }
    @Test
    void userShouldPatchUserError() {
        final User user = new User();
        user.setEmail("john@firesale.nl");
        user.setDisplayName("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.now());
        user.setPassword("test");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        mockedGuard.when(Guard::isAdmin).thenReturn(false);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);
        Exception exception = assertThrows(UnAuthorizedException.class, () -> userService.patchUser(1L, user));
        assertThat(exception.getMessage()).isEqualTo("User is not authorized for: [Cannot patch user fields]");
        verify(userRepository).findById(any(Long.class));
    }


    @Test
    void userShouldThrowErrorWhenPatchingUser() {
        final User user = new User();
        user.setEmail("john@firesale.nl");
        user.setDisplayName("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockedGuard.when(Guard::isAdmin).thenReturn(false);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);

        Exception exception = assertThrows(UnAuthorizedException.class, () -> userService.patchUser(1L, user));

        assertThat(exception.getMessage()).isEqualTo("User is not authorized for: [Cannot patch user fields]");

        verify(userRepository).findById(any(Long.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void canDelete() {
        final User user = new User();
        user.setId(1L);
        user.setEmail("john@firesale.nl");
        user.setDisplayName("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.now());
        user.setPassword("test");
        var auctions = Collections.emptyList();
        var bids = Collections.emptyList();
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
        doReturn(auctions).when(auctionRepository).findByUserIdAndIsDeletedFalseOrderByEndDateDesc(anyLong());
        doReturn(bids).when(bidRepository).findByUserId(anyLong());
        doNothing().when(userRepository).delete(user);
        userService.delete( user);
        verify(userRepository).delete(any(User.class));
        verify(userRepository).findById(any(Long.class));
        verify(auctionRepository).findByUserIdAndIsDeletedFalseOrderByEndDateDesc(any(Long.class));
        verify(bidRepository).findByUserId(any(Long.class));
    }

    @Test
    void anonymous1() {
        final User user = new User();
        user.setId(1L);
        user.setEmail("john@firesale.nl");
        user.setDisplayName("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.now());
        user.setPassword("test");
        var auctions = Collections.singletonList(new Auction());
        var bids = Collections.emptyList();
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
        doReturn(auctions).when(auctionRepository).findByUserIdAndIsDeletedFalseOrderByEndDateDesc(anyLong());
        doReturn(bids).when(bidRepository).findByUserId(anyLong());
        doReturn(user).when(userRepository).save(user);
        userService.delete( user);
        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(any(Long.class));
        verify(auctionRepository).findByUserIdAndIsDeletedFalseOrderByEndDateDesc(any(Long.class));
        verify(bidRepository).findByUserId(any(Long.class));
    }

    @Test
    void anonymous2() {
        final User user = new User();
        user.setId(1L);
        user.setEmail("john@firesale.nl");
        user.setDisplayName("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.now());
        user.setPassword("test");
        var auctions = Collections.emptyList();
        var bids = Collections.singletonList(new Bid());
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
        doReturn(auctions).when(auctionRepository).findByUserIdAndIsDeletedFalseOrderByEndDateDesc(anyLong());
        doReturn(bids).when(bidRepository).findByUserId(anyLong());
        doReturn(user).when(userRepository).save(user);
        userService.delete( user);
        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(any(Long.class));
        verify(auctionRepository).findByUserIdAndIsDeletedFalseOrderByEndDateDesc(any(Long.class));
        verify(bidRepository).findByUserId(any(Long.class));
    }


    @Test
    void notFoundDelete() {
        final User user = new User();
        user.setId(1L);
        user.setEmail("john@firesale.nl");
        user.setDisplayName("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.now());
        user.setPassword("test");
        doReturn(Optional.empty()).when(userRepository).findById(anyLong());
        String expected = "Resource was not found: [No user exists for id: 1]";
        var exception = assertThrows(ResourceNotFoundException.class, () -> userService.delete(user));
        verify(userRepository).findById(anyLong());
        assertEquals(expected, exception.getMessage(), "Incorrect message");
    }




    private User getUser(){
        final User user = new User();
        user.setId(1L);
        user.setEmail("admin@firesale.nl");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setAddress(new Address());
        return user;
    }
}
