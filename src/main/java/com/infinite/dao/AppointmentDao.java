package com.infinite.dao;

import com.infinite.model.Appointment;

public interface AppointmentDao {

	public String bookAnAppointment(Appointment appointment);
	public boolean isAvailabilitySlotFull(String availabilityId);


}
