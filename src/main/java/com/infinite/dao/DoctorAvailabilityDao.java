package com.infinite.dao;

import com.infinite.model.DoctorAvailability;

public interface DoctorAvailabilityDao {

	public DoctorAvailability searchDoctorAvailabilityByDoctorId(String doctorId);
}
