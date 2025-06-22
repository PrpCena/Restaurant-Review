package com.prp.rr_be.services.impl;

import com.prp.rr_be.domain.GeoLocation;
import com.prp.rr_be.domain.RestaurantCreateUpdateRequest;
import com.prp.rr_be.domain.entities.Address;
import com.prp.rr_be.domain.entities.Photo;
import com.prp.rr_be.domain.entities.Restaurant;
import com.prp.rr_be.exception.RestaurantNotFoundException;
import com.prp.rr_be.repositories.RestaurantRepository;
import com.prp.rr_be.services.GeoLocationService;
import com.prp.rr_be.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl
  implements RestaurantService {
  
  private final RestaurantRepository restaurantRepository;
  private final GeoLocationService geoLocationService;
  
  @Override
  public Restaurant createRestaurant(RestaurantCreateUpdateRequest request) {
	Address address = request.getAddress();
	GeoLocation geoLocation = geoLocationService.geoLocate(address);
	GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());
	List<String> photoIds = request.getPhotoIds();
	List<Photo> photos = photoIds
						   .stream()
						   .map(url -> Photo
										 .builder()
										 .url(url)
										 .uploadDate(LocalDateTime.now())
										 .build())
						   .toList();
	Restaurant restaurant = Restaurant
							  .builder()
							  .name(request.getName())
							  .cuisineType(request.getCuisineType())
							  .address(address)
							  .photos(photos)
							  .contactInformation(request.getContactInformation())
							  .geoLocation(geoPoint)
							  .operatingHours(request.getOperatingHours())
							  .averageRating(0f)
							  .build();
	
	return restaurantRepository.save(restaurant);
	
  }
  
  @Override
  public Page<Restaurant> searchRestaurants(
	String query,
	Float minRating,
	Float latitude,
	Float longitude,
	Float radius,
	Pageable pageable) {
	// If just filtering my min rating
	if (null != minRating && (null == query || query.isEmpty())) {
	  return restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
	}
	// Normalize min rating to be used in other queries
	Float searchMinRating = minRating == null ? 0f : minRating;
	// If there's a text, search query
	if (query != null && !query
							.trim()
							.isEmpty()) {
	  return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
	}
	// If there's a location search
	if (latitude != null && longitude != null && radius != null) {
	  return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
	}
	// Otherwise we'll perform a non-location search
	return restaurantRepository.findAll(pageable);
  }
  
  @Override
  public Optional<Restaurant> getRestaurant(String id) {
	return restaurantRepository.findById(id);
  }
  
  @Override
  public Restaurant updateRestaurant(String id, RestaurantCreateUpdateRequest request) {
	Restaurant restaurant = restaurantRepository
							  .findById(id)
							  .orElseThrow(() -> new RestaurantNotFoundException("restaurant not found with id" + id));
	GeoLocation newGeoLocation = geoLocationService.geoLocate(request.getAddress());
	GeoPoint newGeoPoint = new GeoPoint(newGeoLocation.getLatitude(), newGeoLocation.getLongitude());
	List<String> photoIds = request.getPhotoIds();
	List<Photo> photos = photoIds
						   .stream()
						   .map(url -> Photo
										 .builder()
										 .url(url)
										 .uploadDate(LocalDateTime.now())
										 .build())
						   .toList();
	restaurant.setName(request.getName());
	restaurant.setAddress(request.getAddress());
	restaurant.setCuisineType(request.getCuisineType());
	restaurant.setPhotos(photos);
	restaurant.setContactInformation(request.getContactInformation());
	restaurant.setGeoLocation(newGeoPoint);
	restaurant.setOperatingHours(request.getOperatingHours());
	
	
	return restaurantRepository.save(restaurant);
  }
  
  @Override
  public void deleteAllRestaurants() {
	restaurantRepository.deleteAll();
  }
  
  @Override
  public void deleteRestaurant(String id) {
	restaurantRepository.deleteById(id);
  }
}
