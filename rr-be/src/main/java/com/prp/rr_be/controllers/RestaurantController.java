package com.prp.rr_be.controllers;

import com.prp.rr_be.domain.RestaurantCreateUpdateRequest;
import com.prp.rr_be.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.prp.rr_be.domain.dtos.RestaurantDto;
import com.prp.rr_be.domain.dtos.RestaurantSummaryDto;
import com.prp.rr_be.domain.entities.Restaurant;
import com.prp.rr_be.mappers.RestaurantMapper;
import com.prp.rr_be.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/restaurants")
public class RestaurantController {
  
  private final RestaurantService restaurantService;
  private final RestaurantMapper restaurantMapper;
  
  @PostMapping
  public ResponseEntity<RestaurantDto> createRestaurant(@RequestBody @Valid RestaurantCreateUpdateRequestDto request) {
	RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.toRestaurantCreateUpdateRequest(
	  request);
	Restaurant restaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest);
	RestaurantDto restaurantDto = restaurantMapper.toRestaurantDto(restaurant);
	return ResponseEntity.ok(restaurantDto);
  }
  
  @GetMapping
  public Page<RestaurantSummaryDto> searchRestaurants(
	@RequestParam(required = false) String q,
	@RequestParam(required = false) Float minRating,
	@RequestParam(required = false) Float latitude,
	@RequestParam(required = false) Float longitude,
	@RequestParam(required = false) Float radius,
	@RequestParam(defaultValue = "1") int page,
	@RequestParam(defaultValue = "20") int size) {
	Page<Restaurant> searchResult = restaurantService.searchRestaurants(
	  q, minRating, latitude, longitude, radius,
	  PageRequest.of(page - 1, size));
	return searchResult.map(restaurantMapper::toSummaryDto);
  }
  
  @GetMapping(path = "/{id}")
  public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable String id) {
	return restaurantService
			 .getRestaurant(id)
			 .map(res -> ResponseEntity.ok(restaurantMapper.toRestaurantDto(res)))
			 .orElse(ResponseEntity
					   .notFound()
					   .build());
  }
  
  @PutMapping(path = "/{id}")
  public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable String id, @RequestBody @Valid RestaurantCreateUpdateRequestDto request) {
	RestaurantCreateUpdateRequest  restaurantCreateUpdateRequest = restaurantMapper.toRestaurantCreateUpdateRequest(request);
	Restaurant restaurant = restaurantService.updateRestaurant(id, restaurantCreateUpdateRequest);
	RestaurantDto restaurantDto = restaurantMapper.toRestaurantDto(restaurant);
	return ResponseEntity.ok(restaurantDto);
  }
  
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> deleteRestaurant(@PathVariable("id") String id) {
	restaurantService.deleteRestaurant(id);
	return ResponseEntity.noContent().build();
  }
  
}
