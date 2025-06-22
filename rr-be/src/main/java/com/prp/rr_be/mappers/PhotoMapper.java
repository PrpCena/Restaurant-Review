package com.prp.rr_be.mappers;


import com.prp.rr_be.domain.dtos.PhotoDto;
import com.prp.rr_be.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {
  PhotoDto toDto(Photo photo);
  
}
