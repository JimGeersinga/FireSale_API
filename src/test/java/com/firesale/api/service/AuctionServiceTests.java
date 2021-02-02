package com.firesale.api.service;

import com.firesale.api.dto.TagDTO;
import com.firesale.api.dto.auction.AuctionFilterDTO;
import com.firesale.api.dto.auction.CreateAuctionDTO;
import com.firesale.api.exception.ResourceNotFoundException;
import com.firesale.api.exception.UnAuthorizedException;
import com.firesale.api.mapper.AuctionMapper;
import com.firesale.api.model.*;
import com.firesale.api.model.Tag;
import com.firesale.api.repository.AuctionRepository;
import com.firesale.api.repository.CategoryRepository;
import com.firesale.api.repository.FavouriteAuctionRepository;
import com.firesale.api.repository.UserRepository;
import com.firesale.api.security.Guard;
import com.firesale.api.security.UserPrincipal;
import com.firesale.api.util.SecurityUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTests {
    @InjectMocks
    private AuctionService auctionService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AuctionMapper auctionMapper;
    @Mock
    private TagService tagService;

    @Mock
    private AuctionNotificationService auctionNotificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FavouriteAuctionRepository favouriteAuctionRepository;

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
    @DisplayName("Test findById Success")
    void testFindById() {
        // Setup mock repository
        Auction auction = new Auction();
        doReturn(Optional.of(auction)).when(auctionRepository).findById(1L);

        // Execute service call
        Optional<Auction> returnedAuction = Optional.ofNullable(auctionService.findAuctionById(1));

        // Assert response
        Assertions.assertTrue(returnedAuction.isPresent(), "Auction was not found");
        Assertions.assertSame(returnedAuction.get(), auction, "The auction returned was not the same as the mock");
        verify(auctionRepository).findById(any(Long.class));
    }


    @Test
    @DisplayName("Test filterAuctions 1 Success")
    void filterAuctions1() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();

        long[] ar = {1L, 2L, 3L};
        String[] ars = {"1L", "2L", "3L"};
        filter.setCategories(ar);
        filter.setTags(ars);
        filter.setName("ar");

        var auctions = getEmptyAuctions();

        doReturn(auctions).when(auctionRepository).findAuctionsByTagsLikeAndCategoriesANDNameLike(any(), any(), any());

        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAuctionsByTagsLikeAndCategoriesANDNameLike(any(), any(), any());
    }

    @Test
    @DisplayName("Test filterAuctions 2 Success")
    void filterAuctions2() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();

        long[] ar = {1L, 2L, 3L};
        String[] ars = {"1L", "2L", "3L"};
        filter.setCategories(ar);
        filter.setTags(ars);
        filter.setName(null);

        var auctions = getEmptyAuctions();

        doReturn(auctions).when(auctionRepository).findAuctionsByTagsLikeAndCategoriesLike(any(), any());

        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAuctionsByTagsLikeAndCategoriesLike(any(), any());
    }

    @Test
    @DisplayName("Test filterAuctions 3 Success")
    void filterAuctions3() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();

        long[] ar = {1L, 2L, 3L};
        filter.setCategories(ar);
        filter.setTags(null);
        filter.setName("null");

        var auctions = getEmptyAuctions();

        doReturn(auctions).when(auctionRepository).findAuctionsByCategoriesLikeAndNameLike(any(), any());

        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAuctionsByCategoriesLikeAndNameLike(any(), any());
    }

    @Test
    @DisplayName("Test filterAuctions 4 Success")
    void filterAuctions4() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();

        String[] ars = {"1L", "2L", "3L"};
        filter.setCategories(null);
        filter.setTags(ars);
        filter.setName("null");

        var auctions = getEmptyAuctions();

        doReturn(auctions).when(auctionRepository).findAuctionsByTagsLikeAndNameLike(any(), any());

        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAuctionsByTagsLikeAndNameLike(any(), any());
    }

    @Test
    @DisplayName("Test filterAuctions 5 Success")
    void filterAuctions5() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();

        long[] ar = {1L, 2L, 3L};
        filter.setCategories(ar);
        filter.setTags(null);
        filter.setName(null);

        var auctions = getEmptyAuctions();

        doReturn(auctions).when(auctionRepository).findAuctionsByCategories(any());

        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAuctionsByCategories(any());
    }

    @Test
    @DisplayName("Test filterAuctions 6 Success")
    void filterAuctions6() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();

        String[] ars = {"1L", "2L", "3L"};
        filter.setCategories(null);
        filter.setTags(ars);
        filter.setName(null);

        var auctions = getEmptyAuctions();

        doReturn(auctions).when(auctionRepository).findAuctionsByTags(any());

        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAuctionsByTags(any());
    }

    @Test
    @DisplayName("Test filterAuctions 7 Success")
    void filterAuctions7() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();

        filter.setCategories(null);
        filter.setTags(null);
        filter.setName("null");

        var auctions = getEmptyAuctions();

        doReturn(auctions).when(auctionRepository).findAuctionsByNameLike(any());

        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAuctionsByNameLike(any());
    }

    @Test
    @DisplayName("Test filterAuctions 8 Success")
    void filterAuctions8() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();
        var auctionPage = mock(Page.class);
        long[] ar = {};
        String[] ars = {};
        filter.setCategories(ar);
        filter.setTags(ars);
        filter.setName(null);

        var auctions = getEmptyAuctions();

        doReturn(auctionPage).when(auctionRepository).findActiveAuctions(any());
        doReturn(auctions).when(auctionPage).getContent();
        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findActiveAuctions(any());
    }

    @Test
    @DisplayName("Test filterAuctions 9 Success")
    void filterAuctions9() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();
        var auctionPage = mock(Page.class);
        String[] ars = {};
        filter.setCategories(null);
        filter.setTags(ars);
        filter.setName(null);

        var auctions = getEmptyAuctions();

        doReturn(auctionPage).when(auctionRepository).findActiveAuctions(any());
        doReturn(auctions).when(auctionPage).getContent();
        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findActiveAuctions(any());
    }

    @Test
    @DisplayName("Test filterAuctions 10 Success")
    void filterAuctions10() {
        // Setup mock repository
        AuctionFilterDTO filter = new AuctionFilterDTO();
        var auctionPage = mock(Page.class);
        long[] ar = {};
        filter.setCategories(ar);
        filter.setTags(null);
        filter.setName(null);

        var auctions = getEmptyAuctions();

        doReturn(auctionPage).when(auctionRepository).findActiveAuctions(any());
        doReturn(auctions).when(auctionPage).getContent();
        // Execute service call
        var returnedAuctions = auctionService.filterAuctions(filter);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findActiveAuctions(any());
    }

    @Test
    @DisplayName("Test getAuctions Success")
    void getAuctions() {
        // Setup mock repository
        var auctions = getEmptyAuctions();
        doReturn(auctions).when(auctionRepository).findAll();
        // Execute service call
        var returnedAuctions = auctionService.getAuctions();

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAll();
    }

    @Test
    @DisplayName("Test getFavourites Success")
    void getFavourites() {
        // Setup mock repository
        var auctions = getEmptyAuctions();
        doReturn(auctions).when(auctionRepository).findAuctionsByFavourite(1L);
        // Execute service call
        var returnedAuctions = auctionService.getFavourites(1L);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findAuctionsByFavourite(1L);
    }

    @Test
    @DisplayName("Test getByUserBid Success")
    void getByUserBid() {
        // Setup mock repository
        var auctions = getEmptyAuctions();
        doReturn(auctions).when(auctionRepository).findActiveByUserBid(1L);
        // Execute service call
        var returnedAuctions = auctionService.getByUserBid(1L);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findActiveByUserBid(1L);
    }

    @Test
    @DisplayName("Test getWonAuction Success")
    void getWonAuction() {
        // Setup mock repository
        var auctions = getEmptyAuctions();
        doReturn(auctions).when(auctionRepository).findWonByUserBid(1L);
        // Execute service call
        var returnedAuctions = auctionService.getWonAuction(1L);

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertSame(auctions, returnedAuctions, "The auction returned was not the same as the mock");
        verify(auctionRepository).findWonByUserBid(1L);
    }

    @Test
    @DisplayName("Test isFavourite Success")
    void isFavourite() {
        // Setup mock repository
        var auctions = getEmptyAuction(1L);
        doReturn(Optional.of(auctions)).when(favouriteAuctionRepository).findByAuctionIdAndUserId(1L, 1L);
        // Execute service call
        var returnedAuctions = auctionService.isFavourite(1L, 1L);

        // Assert response
        Assertions.assertTrue(returnedAuctions, "Auctions not found");
        verify(favouriteAuctionRepository).findByAuctionIdAndUserId(1L, 1L);
    }

    @Test
    @DisplayName("Test getFeatured Success")
    void getFeatured() {
        // Setup mock repository
        var auctions = getFiveAuctions();
        doReturn(auctions).when(auctionRepository).findActiveAuctionsByIsFeaturedTrue();
        var auctionPage = mock(Page.class);

        doReturn(auctionPage).when(auctionRepository).findActiveAuctionsByIsFeaturedFalse(any());
        doReturn(auctions).when(auctionPage).getContent();

        // Execute service call
        var returnedAuctions = auctionService.getFeatured();

        // Assert response
        Assertions.assertFalse(returnedAuctions.isEmpty(), "Auctions not found");
        Assertions.assertEquals(10, (long) returnedAuctions.size(), "Auctions not found");

        verify(auctionRepository).findActiveAuctionsByIsFeaturedTrue();
        verify(auctionRepository).findActiveAuctionsByIsFeaturedFalse(any());

    }




    @Test
    @DisplayName("Test getActiveAuctions Success")
    void testFindActive() {
        // Setup mock repository
        Auction a = new Auction();
        a.setId(1L);
        a.setName("test");
        a.setBids(new ArrayList<>());
        a.setTags(new ArrayList<>());
        a.setEndDate(LocalDateTime.now().plusDays(1));
        a.setStartDate(LocalDateTime.now());
        a.setStatus(AuctionStatus.READY);
        a.setDescription("Description");
        List<Auction> auctions = (Collections.singletonList(a));
        PageImpl<Auction> pagedAuctions = new PageImpl<>(auctions);
        doReturn(pagedAuctions).when(auctionRepository).findActiveAuctions(PageRequest.of(0, 10));
        // Execute service call
        Collection<?> returnedAuction = auctionService.getActiveAuctions(PageRequest.of(0, 10));
        // Assert response
        Assertions.assertNotNull(returnedAuction, "Auction was not found");
        Assertions.assertSame(returnedAuction.toArray()[0], a, "The auction returned was not the same as the mock");
        verify(auctionRepository).findActiveAuctions(any(Pageable.class));
    }

    @Test
    @DisplayName("Test findByUserId Success")
    void testFindByUserId() {
        // Setup mock repository
        Auction a = new Auction();
        a.setId(1L);
        a.setName("test");
        a.setBids(new ArrayList<>());
        a.setTags(new ArrayList<>());
        a.setEndDate(LocalDateTime.now().plusDays(1));
        a.setStartDate(LocalDateTime.now());
        a.setStatus(AuctionStatus.READY);
        a.setDescription("Description");
        List<Auction> auctions = (Collections.singletonList(a));
        doReturn(auctions).when(auctionRepository).findByUserIdAndIsDeletedFalseOrderByEndDateDesc(1L);
        // Execute service call
        var returnedAuction = auctionService.getAuctionsByUserId(1L);
        // Assert response
        Assertions.assertNotNull(returnedAuction, "Auction was not found");
        Assertions.assertSame(returnedAuction.toArray()[0], a, "The auction returned was not the same as the mock");
        verify(auctionRepository).findByUserIdAndIsDeletedFalseOrderByEndDateDesc(any(Long.class));
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        // Setup mock repository
        doReturn(Optional.empty()).when(auctionRepository).findById(1L);
        // Execute service call
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> auctionService.findAuctionById(1L));
        // Assert response
        assertThat(exception.getMessage()).isEqualTo("Resource was not found: [No auction exists for id: 1]");
        verify(auctionRepository).findById(any(Long.class));
    }

    @Test
    @DisplayName("Test deleteAuctionNotCancelled Not Found")
    void deleteAuctionNotCancelled() {
        // Setup mock repository
        mockedGuard.when(Guard::isAdmin).thenReturn(true);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);
        var auction = getEmptyAuction(1L);
        var user = new User();
        user.setId(1L);
        auction.setUser(user);
        auction.setStatus(AuctionStatus.READY);
        doReturn(Optional.of(auction)).when(auctionRepository).findById(1L);
        // Execute service call
        Exception exception = assertThrows(UnAuthorizedException.class, () -> auctionService.deleteAuction(1L));
        // Assert response
        assertThat(exception.getMessage()).isEqualTo("User is not authorized for: [Cannot delete the auction]");
        verify(auctionRepository).findById(any(Long.class));
    }

    @Test
    @DisplayName("Test deleteAuctionNotAllowedNot Found")
    void deleteAuctionNotAllowed() {
        // Setup mock repository
        mockedGuard.when(Guard::isAdmin).thenReturn(false);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);
        var auction = getEmptyAuction(1L);
        var user = new User();
        user.setId(1L);
        auction.setUser(user);
        auction.setStatus(AuctionStatus.CANCELLED);
        doReturn(Optional.of(auction)).when(auctionRepository).findById(1L);
        // Execute service call
        Exception exception = assertThrows(UnAuthorizedException.class, () -> auctionService.deleteAuction(1L));
        // Assert response
        assertThat(exception.getMessage()).isEqualTo("User is not authorized for: [Cannot delete the auction]");
        verify(auctionRepository).findById(any(Long.class));
    }

    @Test
    @DisplayName("Test deleteAuction Success")
    void deleteAuction() {
        // Setup mock repository
        mockedGuard.when(Guard::isAdmin).thenReturn(false);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(true);
        var auction = getEmptyAuction(1L);
        var user = new User();
        user.setId(1L);
        auction.setUser(user);
        auction.setStatus(AuctionStatus.CANCELLED);
        doReturn(Optional.of(auction)).when(auctionRepository).findById(1L);
        doNothing().when(auctionNotificationService).sendStatusNotification(anyLong(), any(AuctionStatus.class));
        when(auctionRepository.save(any(Auction.class))).thenAnswer((answer) -> answer.getArguments()[0]);
        // Execute service call
        var returnedAuction = auctionService.deleteAuction(1L);
        // Assert response
        assertThat(returnedAuction.getStatus()).isEqualTo(AuctionStatus.CLOSED);
        verify(auctionRepository).findById(any(Long.class));
    }



    @Test
    @DisplayName("Test create Success")
    void create() {
        // Setup mock repository
        var user = new User();
        user.setId(1L);
        UserPrincipal principal = new UserPrincipal(user);



        try(MockedStatic<SecurityUtil> dummy = Mockito.mockStatic(SecurityUtil.class)){
            dummy.when(SecurityUtil::getSecurityContextUser).thenReturn(principal);
            doReturn(getEmptyCategories()).when(categoryRepository).findByIdIn(any());
            when(auctionMapper.toModel(any(CreateAuctionDTO.class))).thenAnswer((answer) -> new Auction());
            doReturn(new User()).when(userRepository).getOne(any(Long.class));
            when(tagService.getTagByName(anyString())).thenReturn(null, new Tag());
            doReturn(new Tag()).when(tagService).createTag(any(String.class));
            when(auctionRepository.save(any(Auction.class))).thenAnswer((answer) -> answer.getArguments()[0]);
            CreateAuctionDTO dto = new CreateAuctionDTO();
            var tag1 = new TagDTO();
            tag1.setName("test");
            var tag2 = new TagDTO();
            tag2.setName("test2");
            dto.setTags(Arrays.asList(tag1, tag2));
            dto.setCategories(Arrays.asList(1L , 2L));

            auctionService.create(dto);
            verify(auctionRepository).save(any(Auction.class));
            verify(tagService).createTag(any(String.class));
            verify(tagService).createTag(any(String.class));
        }
    }


    @Test
    @DisplayName("Test patch empty Success")
    void testPatchEmpty() {
        // Setup mock repository
        mockedGuard.when(Guard::isAdmin).thenReturn(true);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);
        Auction auction = this.getEmptyAuction(1L);
        User u = new User();
        u.setId(1L);
        auction.setUser(u);
        doReturn(Optional.of(auction)).when(auctionRepository).findById(anyLong());
        when(auctionRepository.save(any(Auction.class))).thenAnswer((answer) -> answer.getArguments()[0]);
        // Execute service call
        var returnedAuction = auctionService.patchAuction(1L, auction);
        // Assert response
        Assertions.assertNotNull(returnedAuction, "Auction was not found");
        Assertions.assertSame(auction.getId(), returnedAuction.getId(), "The auction returned was not the same as the mock");

    }



    @Test
    @DisplayName("Test toggleFavourite Success")
    void toggleFavouriteTrue() {
        // Setup mock repository
        FavouriteAuction auction = new FavouriteAuction();
        doReturn(Optional.of(auction)).when(favouriteAuctionRepository).findByAuctionIdAndUserId(anyLong(), anyLong());
        // Execute service call
        auctionService.toggleFavourite(1L, 1L, true);
        // Assert response
        verify(favouriteAuctionRepository).findByAuctionIdAndUserId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Test toggleFavourite Success")
    void toggleFavouriteFalse() {
        // Setup mock repository
        FavouriteAuction auction = new FavouriteAuction();
        doReturn(Optional.of(auction)).when(favouriteAuctionRepository).findByAuctionIdAndUserId(anyLong(), anyLong());
        doNothing().when(favouriteAuctionRepository).delete(any(FavouriteAuction.class));
        // Execute service call
        auctionService.toggleFavourite(1L, 1L, false);
        // Assert response
        verify(favouriteAuctionRepository).delete(any(FavouriteAuction.class));
        verify(favouriteAuctionRepository).findByAuctionIdAndUserId(anyLong(), anyLong());
    }


    @Test
    @DisplayName("Test toggleFavourite Success")
    void toggleFavouriteTrueSave() {
        // Setup mock repository
        doReturn(Optional.empty()).when(favouriteAuctionRepository).findByAuctionIdAndUserId(anyLong(), anyLong());
        // Execute service call
        auctionService.toggleFavourite(1L, 1L, false);
        // Assert response
        verify(favouriteAuctionRepository).findByAuctionIdAndUserId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Test toggleFavourite Success")
    void toggleFavouriteFalseSave() {
        // Setup mock repository
        FavouriteAuction auction = new FavouriteAuction();

        doReturn(Optional.empty()).when(favouriteAuctionRepository).findByAuctionIdAndUserId(anyLong(), anyLong());
        doReturn(auction).when(favouriteAuctionRepository).save(any(FavouriteAuction.class));
        // Execute service call
        auctionService.toggleFavourite(1L, 1L, true);
        // Assert response
        verify(favouriteAuctionRepository).save(any(FavouriteAuction.class));
        verify(favouriteAuctionRepository).findByAuctionIdAndUserId(anyLong(), anyLong());
    }





    @Test
    @DisplayName("Test update Success")
    void testUpdateEmpty() {
        // Setup mock repository
        mockedGuard.when(Guard::isAdmin).thenReturn(true);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);
        Auction auction = getEmptyAuction(1L);
        auction.setStartDate(LocalDateTime.now().plusHours(10));

        CreateAuctionDTO auctionDTO = new CreateAuctionDTO();
        User u = new User();
        u.setId(1L);
        auctionDTO.setId(1L);
        when(tagService.getTagByName(anyString())).thenReturn(null, new Tag());
        doReturn(new Tag()).when(tagService).createTag(anyString());
        doReturn(Optional.of(auction)).when(auctionRepository).findById(anyLong());
        when(auctionRepository.save(any(Auction.class))).thenAnswer((answer) -> answer.getArguments()[0]);

        var categories = this.getEmptyCategories();
        auctionDTO.setCategories(Arrays.asList(1L, 2L));
        doReturn(categories).when(categoryRepository).findByIdIn(any());


        var tag1 = new TagDTO();
        tag1.setName("test");
        var tag2 = new TagDTO();
        tag2.setName("test2");

        auctionDTO.setTags(Arrays.asList(tag1, tag2));

        // Execute service call
        var returnedAuction = auctionService.updateAuction(1L, auctionDTO);
        // Assert response
        Assertions.assertNotNull(returnedAuction, "Auction was not found");
        Assertions.assertSame(auction.getId(), returnedAuction.getId(), "The auction returned was not the same as the mock");

    }


    @Test
    @DisplayName("Test update Failure")
    void testupdateEmptyFailure() {
        // Setup mock repository
        Auction auction = getEmptyAuction(1L);
        auction.setStartDate(LocalDateTime.now().minusHours(10));

        CreateAuctionDTO auctionDTO = new CreateAuctionDTO();
        User u = new User();
        u.setId(1L);
        auctionDTO.setId(1L);
        doReturn(Optional.of(auction)).when(auctionRepository).findById(anyLong());

        var categories = this.getEmptyCategories();
        auctionDTO.setCategories(Arrays.asList(1L, 2L));
        doReturn(categories).when(categoryRepository).findByIdIn(any());


        String expected = "User is not authorized for: [Cannot change a running auction]";
        // Execute service call
        var returnedAuction = Assertions.assertThrows(UnAuthorizedException.class, () ->auctionService.updateAuction(1L, auctionDTO));
        // Assert response
        Assertions.assertEquals(returnedAuction.getMessage(), expected, "Auction was not found");

    }




    @Test
    @DisplayName("Test patch empty Failure")
    void testPatchFailure() {
        // Setup mock repository
        mockedGuard.when(Guard::isAdmin).thenReturn(false);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);
        Auction auction = this.getEmptyAuction(1L);
        User u = new User();
        u.setId(1L);
        auction.setUser(u);
        doReturn(Optional.of(auction)).when(auctionRepository).findById(anyLong());

        String expected = "User is not authorized for: [You are not allowed to cancel this auction]";
        // Execute service call
        var returnedException = Assertions.assertThrows(UnAuthorizedException.class, () -> auctionService.patchAuction(1L, auction));
        // Assert response
        Assertions.assertEquals(expected, returnedException.getMessage(), "Exception is not correct");

    }

    @Test
    @DisplayName("Test patch Success")
    void testPatch() {
        // Setup mock repository
        mockedGuard.when(Guard::isAdmin).thenReturn(true);
        mockedGuard.when(() -> Guard.isSelf(anyLong())).thenReturn(false);
        Auction auction = this.getEmptyAuction(1L);
        User u = new User();
        u.setId(1L);
        auction.setUser(u);


        auction.setStartDate(LocalDateTime.of(3000, 1, 1,1 , 1));
        auction.setName("test");
        auction.setDescription("test");
        auction.setDescription("test");
        auction.setEndDate(LocalDateTime.of(3000, 1, 1,1 , 1));
        auction.setMinimalBid(10d);
        auction.setIsFeatured(true);
        auction.setStatus(AuctionStatus.CLOSED);


        doReturn(Optional.of(auction)).when(auctionRepository).findById(anyLong());

        when(auctionRepository.save(any(Auction.class))).thenAnswer((answer) -> {
            return (Auction)answer.getArguments()[0];
        });
        doNothing().when(auctionNotificationService).sendStatusNotification(anyLong(),any(AuctionStatus.class));

        // Execute service call
        var returnedAuction = auctionService.patchAuction(1L, auction);

        // Assert response
        Assertions.assertNotNull(returnedAuction, "Auction was not found");
        Assertions.assertSame(auction.getId(), returnedAuction.getId(), "The auction returned was not the same as the mock");

    }

    private Category getEmpty(Long id)
    {
        Category category = new Category();
        category.setName("test " + id);
        category.setId(id);
        category.setArchived(false);
        category.setAuctions(new ArrayList<>());
        return category;
    }



    private List<Category> getEmptyCategories()
    {
        ArrayList<Category> categories = new ArrayList<>();
        for(int i = 1; i <= 10; i++)
        {
            var c = getEmpty((long) i);
            categories.add(c);
        }
        return categories;
    }

    private Auction getEmptyAuction(Long id)
    {
        Auction auction = new Auction();
        auction.setName("test " + id);
        auction.setId(id);
        return auction;
    }



    private List<Auction> getEmptyAuctions()
    {
        ArrayList<Auction> auctions = new ArrayList<>();
        for(int i = 1; i <= 10; i++)
        {
            var auction = getEmptyAuction((long) i);
            auctions.add(auction);
        }
        return auctions;
    }

    private List<Auction> getFiveAuctions()
    {
        ArrayList<Auction> auctions = new ArrayList<>();
        for(int i = 1; i <= 5; i++)
        {
            var auction = getEmptyAuction((long) i);
            auctions.add(auction);
        }
        return auctions;
    }

}
