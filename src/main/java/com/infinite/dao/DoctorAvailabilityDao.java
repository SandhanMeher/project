package com.infinite.dao;

import java.sql.Date;
import java.util.List;
import com.infinite.model.DoctorAvailability;

public interface DoctorAvailabilityDao {
    // Fetch all available timings for a doctor on a specific date
    List<DoctorAvailability> getAvailableSlotsByDoctorAndDate(String doctorId, Date date);

    // (Optional - for reuse) Get availability by ID
    DoctorAvailability getAvailabilityById(String availabilityId);
}
