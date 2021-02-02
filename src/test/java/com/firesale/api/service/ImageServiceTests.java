package com.firesale.api.service;

import com.firesale.api.dto.auction.CreateImageDTO;
import com.firesale.api.exception.ResourceNotFoundException;
import com.firesale.api.model.Auction;
import com.firesale.api.model.Image;
import com.firesale.api.model.User;
import com.firesale.api.repository.AuctionRepository;
import com.firesale.api.repository.ImageRepository;
import com.firesale.api.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTests {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private ImageService imageService;


    @Test
    @DisplayName("Test saveImage notFound Failure")
    void saveImageNotFoundFailure() {
        // Setup mock repository
        CreateImageDTO dto = this.getImageDTO(1L);
        doReturn(Optional.empty()).when(imageRepository).findById(1L);
        String expected = "Resource was not found: [Image should be in the database but it is not]";
        // Execute service call
        var returned = Assertions.assertThrows(ResourceNotFoundException.class, () ->imageService.saveImage(dto));
        String actual = returned.getMessage();
        // Assert response
        verify(imageRepository).findById(1L);
        Assertions.assertEquals(expected, actual, "Error message is incorrect");
    }

    @Test
    @DisplayName("Test saveImage existing success")
    void saveImageExistingSuccess() {
        // Setup mock repository
        CreateImageDTO dto = this.getImageDTO(1L);
        doReturn(Optional.of(this.getImage())).when(imageRepository).findById(1L);
        when(imageRepository.save(any(Image.class))).thenAnswer((answer) -> answer.getArguments()[0]);

        // Execute service call
        var returned = imageService.saveImage(dto);
        // Assert response
        verify(imageRepository).findById(1L);
        verify(imageRepository).save(any(Image.class));
        Assertions.assertEquals(1L, (long) returned.getId(), "Incorrect image returned");
    }

    @Test
    @DisplayName("Test saveImage new success")
    void saveImageNewSuccess() {
        // Setup mock repository
        CreateImageDTO dto = this.getImageDTO(null);
        when(imageRepository.save(any(Image.class))).thenAnswer((answer) -> {
            var arg = (Image)answer.getArguments()[0];
            arg.setId(1L);
            return arg;
        });
        // Execute service call
        var returned = imageService.saveImage(dto);
        // Assert response
        verify(imageRepository).save(any(Image.class));
        Assertions.assertEquals(1L, (long) returned.getId(), "Incorrect image");
    }


    @Test
    @DisplayName("Test storeAuctionImages success")
    void storeAuctionImages() {
        // Setup mock repository
        ArrayList<CreateImageDTO> dtos = new ArrayList<>();
        CreateImageDTO dto = this.getImageDTO(null);
        dtos.add(dto);


        when(imageRepository.save(any(Image.class))).thenAnswer((answer) -> {
            var arg = (Image)answer.getArguments()[0];
            arg.setId(1L);
            return arg;
        });
        when(auctionRepository.save(any(Auction.class))).thenAnswer((answer) -> {
            var arg = (Auction)answer.getArguments()[0];
            arg.setId(1L);
            return arg;
        });


        // Execute service call
         imageService.storeAuctionImages(dtos, new Auction());
        // Assert response
        verify(imageRepository).save(any(Image.class));
        verify(auctionRepository).save(any(Auction.class));
    }

    @Test
    @DisplayName("Test storeAvatar success")
    void storeAvatar() {
        // Setup mock repository
        CreateImageDTO dto = this.getImageDTO(null);


        when(imageRepository.save(any(Image.class))).thenAnswer((answer) -> {
            var arg = (Image)answer.getArguments()[0];
            arg.setId(1L);
            return arg;
        });
        when(userRepository.save(any(User.class))).thenAnswer((answer) -> {
            var arg = (User)answer.getArguments()[0];
            arg.setId(1L);
            return arg;
        });

        doReturn(new User()).when(userService).findUserById(1L);


        // Execute service call
        imageService.storeAvatar(dto, 1L);
        // Assert response
        verify(imageRepository).save(any(Image.class));
        verify(userRepository).save(any(User.class));
    }

    private Image getImage()
    {
        Image img = new Image();
        img.setId(1L);
        img.setSort(1);
        img.setPath(new byte[]{});
        img.setType(".jpg");
        return img;
    }

    private CreateImageDTO getImageDTO(Long id)
    {
        CreateImageDTO img = new CreateImageDTO();
        img.setId(id);
        img.setSort(1);
        img.setPath(new byte[]{});
        img.setType(".jpg");
        return img;
    }
}
