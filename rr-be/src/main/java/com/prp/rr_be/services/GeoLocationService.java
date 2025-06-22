package com.prp.rr_be.services;

import com.prp.rr_be.domain.GeoLocation;
import com.prp.rr_be.domain.entities.Address;

public interface GeoLocationService {
	GeoLocation geoLocate(Address address);
}
