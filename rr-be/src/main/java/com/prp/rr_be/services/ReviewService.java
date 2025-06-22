package com.prp.rr_be.services;


import com.prp.rr_be.domain.ReviewCreateUpdateRequest;
import com.prp.rr_be.domain.entities.Review;
import com.prp.rr_be.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {
  Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest request);
  
  Page<Review> listReviews(String restaurantId, Pageable pageable);
  
  Optional<Review> getRestaurantReview(String restaurantId, String reviewId);
  
  Review updateReview(User user, String restaurantId, String reviewId, ReviewCreateUpdateRequest updatedReview);
  
  void deleteReview(String restaurantId, String reviewId);
}
