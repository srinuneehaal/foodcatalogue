package com.codedecode.foodcatalogue.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.sb.foodcatalogue.dto.FoodCataloguePage;
import com.sb.foodcatalogue.dto.FoodItemDTO;
import com.sb.foodcatalogue.dto.Restaurant;
import com.sb.foodcatalogue.entity.FoodItem;
import com.sb.foodcatalogue.mapper.FoodItemMapper;
import com.sb.foodcatalogue.repo.FoodItemRepo;
import com.sb.foodcatalogue.service.FoodCatalogueService;

class FoodCatalogueServiceTest {

    @Mock
    private FoodItemRepo foodItemRepo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FoodCatalogueService foodCatalogueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFoodItem_ShouldSaveFoodItemAndReturnMappedDTO() {
        // Arrange
        FoodItemDTO foodItemDTO = new FoodItemDTO();
        FoodItem foodItem = new FoodItem();
        when(foodItemRepo.save(any(FoodItem.class))).thenReturn(foodItem);

        // Act
        FoodItemDTO result = foodCatalogueService.addFoodItem(foodItemDTO);

        // Assert
        verify(foodItemRepo, times(1)).save(any(FoodItem.class));
        Assertions.assertEquals(FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDto(foodItem), result);
    }

    @Test
    void fetchFoodCataloguePageDetails_ShouldReturnFoodCataloguePage() {
        // Arrange
        int restaurantId = 123;
        List<FoodItem> foodItemList = Arrays.asList(new FoodItem());
        Restaurant restaurant = new Restaurant();
        when(foodItemRepo.findByRestaurantId(restaurantId)).thenReturn(foodItemList);
        when(restTemplate.getForObject(anyString(), eq(Restaurant.class))).thenReturn(restaurant);

        // Act
        FoodCataloguePage result = foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId);

        // Assert
        verify(foodItemRepo, times(1)).findByRestaurantId(restaurantId);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Restaurant.class));
        Assertions.assertEquals(foodItemList, result.getFoodItemsList());
        Assertions.assertEquals(restaurant, result.getRestaurant());
    }
}