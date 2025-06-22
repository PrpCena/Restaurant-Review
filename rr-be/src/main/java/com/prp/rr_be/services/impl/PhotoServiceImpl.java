package com.prp.rr_be.services.impl;


import com.prp.rr_be.domain.entities.Photo;
import com.prp.rr_be.services.PhotoService;
import com.prp.rr_be.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl
		implements PhotoService {
  
  private final StorageService storageService;
  
  @Override
  public Photo uploadPhoto(MultipartFile file) {
	String photoId = UUID.randomUUID()
						 .toString();
	String url = storageService.store(file, photoId);
	return Photo.builder()
				.url(url)
				.uploadDate(LocalDateTime.now())
				.build();
  }
  
  @Override
  public Optional<Resource> getPhotoAsResource(String filename) {
	return storageService.loadResource(filename);
  }
}
