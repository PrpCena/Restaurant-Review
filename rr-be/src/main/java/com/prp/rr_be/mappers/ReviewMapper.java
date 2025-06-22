package com.prp.rr_be.mappers;

import com.prp.rr_be.domain.ReviewCreateUpdateRequest;
import com.prp.rr_be.domain.dtos.ReviewCreateUpdateRequestDto;
import com.prp.rr_be.domain.dtos.ReviewDto;
import com.prp.rr_be.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
  ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto dto);
  
  ReviewDto toDto(Review review);
}