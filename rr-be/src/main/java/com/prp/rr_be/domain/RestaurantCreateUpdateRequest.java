package com.prp.rr_be.domain;

import com.prp.rr_be.domain.entities.Address;
import com.prp.rr_be.domain.entities.OperatingHours;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantCreateUpdateRequest {
	private String name;
	private String cuisineType;
	private String contactInformation;
	private Address address;
	private OperatingHours operatingHours;
	private List<String> photoIds;
}
