package com.firesale.api.service;

import com.firesale.api.aspect.LogDuration;
import com.firesale.api.exception.ResourceNotFoundException;
import com.firesale.api.exception.UnAuthorizedException;
import com.firesale.api.exception.UserRegistrationException;
import com.firesale.api.model.Address;
import com.firesale.api.model.ErrorTypes;
import com.firesale.api.model.Role;
import com.firesale.api.model.User;
import com.firesale.api.repository.AddressRepository;
import com.firesale.api.repository.UserRepository;
import com.firesale.api.repository.AuctionRepository;
import com.firesale.api.repository.BidRepository;
import com.firesale.api.security.Guard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final PasswordEncoder passwordEncoder;

    @LogDuration
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(String.format("No user exists for email: %s", email), ErrorTypes.USER_NOT_FOUND));
    }

    @LogDuration
    public User findUserById(final long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("No user exists for id: %d", userId), ErrorTypes.USER_NOT_FOUND));
    }

    @LogDuration
    public User authenticate(String email, String password) {
        final User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @LogDuration
    @Transactional(readOnly = false)
    public User create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserRegistrationException("An user with this email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsLocked(false);
        user.setRole(Role.USER);

        final Address address = addressRepository.save(user.getAddress());
        user.setAddress(address);

        if (user.getShippingAddress() != null) {
            final Address shippingAddress = addressRepository.save(user.getShippingAddress());
            user.setShippingAddress(shippingAddress);
        }

        return userRepository.save(user);
    }

    @LogDuration
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @LogDuration
    @Transactional(readOnly = false)
    public User updateUser(Long userId, User user) {
        final User existing = findUserById(userId);
        existing.setDisplayName(user.getDisplayName());
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setDateOfBirth(user.getDateOfBirth());
        existing.setEmail(user.getEmail());
        return userRepository.save(existing);
    }

    @LogDuration
    @Transactional(readOnly = false)
    public User patchUser(Long userId, User user) {
        final User existing = findUserById(userId);

        if (Guard.isSelf(userId)) {
            if (user.getEmail() != null)
                existing.setEmail(user.getEmail());
            if (user.getDisplayName() != null)
                existing.setDisplayName(user.getDisplayName());
            if (user.getFirstName() != null)
                existing.setFirstName(user.getFirstName());
            if (user.getLastName() != null)
                existing.setLastName(user.getLastName());
            if (user.getDateOfBirth() != null)
                existing.setDateOfBirth(user.getDateOfBirth());
            if (user.getPassword() != null)
                existing.setPassword(passwordEncoder.encode(user.getPassword()));
        } else if (user.getEmail() != null || user.getDisplayName() != null || user.getFirstName() != null
                || user.getLastName() != null || user.getDateOfBirth() != null || user.getPassword() != null) {
            throw new UnAuthorizedException("Cannot patch user fields");
        }

        if (Guard.isAdmin() && !Guard.isSelf(userId)) {
            if (user.getIsLocked() != null)
                existing.setIsLocked(user.getIsLocked());
            if (user.getRole() != null)
                existing.setRole(user.getRole());
        } else if (user.getIsLocked() != null || user.getRole() != null) {
            throw new UnAuthorizedException("Cannot patch admin fields");
        }

        return userRepository.save(existing);
    }

    @Transactional(readOnly = false)
    public void delete(User user) {
        var userToDelete = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException(
                String.format("No user exists for id: %s", user.getId()), ErrorTypes.USER_NOT_FOUND));
        var bids = bidRepository.findByUserId(userToDelete.getId());
        var auctions = auctionRepository.findByUserIdAndIsDeletedFalseOrderByEndDateDesc(user.getId());
        if (bids.isEmpty() && auctions.isEmpty()) {
            userRepository.delete(userToDelete);
        } else {
            String removed = "removed";
            user.setEmail(removed);
            user.setFirstName(removed);
            user.setLastName(removed);
            user.setAddress(new Address());
            user.setPassword("xxx");
            userRepository.save(user);
        }
    }
}
