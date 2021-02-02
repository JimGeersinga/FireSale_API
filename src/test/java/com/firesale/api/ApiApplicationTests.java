package com.firesale.api;

import com.firesale.api.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ApiApplicationTests {

	@Autowired
	AddressService addressService;

	@Autowired
	AuctionNotificationService auctionNotificationService;

	@Autowired
	AuctionService auctionService;

	@Autowired
	BidService bidService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ImageService imageService;

	@Autowired
	TagService tagService;

	@Autowired
	UserDetailService userDetailService;

	@Autowired
	UserSecurityService userSecurityService;

	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
		assertThat(addressService).isNotNull();
		assertThat(auctionNotificationService).isNotNull();
		assertThat(bidService).isNotNull();
		assertThat(imageService).isNotNull();
		assertThat(tagService).isNotNull();
		assertThat(userDetailService).isNotNull();
		assertThat(userSecurityService).isNotNull();
		assertThat(userService).isNotNull();
	}
}
