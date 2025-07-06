package com.infinite.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.infinite.dao.AppointmentDao;
import com.infinite.dao.AppointmentDaoImpl;
import com.infinite.model.*;


public class AppointmentController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String availabilityId = "A2012"; // default value
	private int selectedSlot;
	private Appointment appointment = new Appointment();
	private List<Integer> availableSlots;

	private String message;
	private boolean success;

	private AppointmentDao appointmentDao = new AppointmentDaoImpl();

	// Load available slot numbers from DAO
	public void loadAvailableSlots() {
		availableSlots = appointmentDao.getAvailableSlotNumbers(availabilityId);
		message = availableSlots.isEmpty() ? "❌ No available slots for Availability ID " + availabilityId
				: "✅ Loaded " + availableSlots.size() + " available slot(s)";
		success = !availableSlots.isEmpty();
	}

	// Book appointment
	public String bookAppointment() {
		try {
			DoctorAvailability availability = new DoctorAvailability();
			availability.setAvailability_id(availabilityId);
			availability.setMax_capacity(4); // hardcoded for now

			Doctors doctor = new Doctors();
			doctor.setDoctor_id("D1003");

			Providers provider = new Providers();
			provider.setProvider_id("P1003");

			Recipient recipient = new Recipient();
			recipient.setH_id("H1003");

			appointment.setDoctor(doctor);
			appointment.setProvider(provider);
			appointment.setRecipient(recipient);
			appointment.setAvailability(availability);
			appointment.setSlot_no(selectedSlot);
			appointment.setAppointment_id("APPT" + System.currentTimeMillis());

			Timestamp start = Timestamp.valueOf("2025-07-09 10:30:00");
			Timestamp end = Timestamp.valueOf("2025-07-09 11:30:00");
			int maxCapacity = availability.getMax_capacity();

			long window = (end.getTime() - start.getTime()) / (1000 * 60) / maxCapacity;
			long slotStart = start.getTime() + (selectedSlot - 1) * window * 60 * 1000;
			long slotEnd = slotStart + window * 60 * 1000;

			appointment.setStart(new Timestamp(slotStart));
			appointment.setEnd(new Timestamp(slotEnd));
			appointment.setRequested_at(Timestamp.valueOf(LocalDateTime.now()));
			appointment.setBooked_at(Timestamp.valueOf(LocalDateTime.now()));
			appointment.setStatus(AppointmentStatus.BOOKED);
			appointment.setNotes("Booked via Controller");

			String result = appointmentDao.bookAnAppointment(appointment);

			if (result.startsWith("Appointment booked")) {
				success = true;
				message = "✅ " + result;
				loadAvailableSlots(); // refresh slot list after booking
			} else {
				success = false;
				message = "❌ Booking failed: " + result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "❌ Internal error: " + e.getMessage();
		}
		return null; // stay on same page
	}

	// Getters and Setters
	public String getAvailabilityId() {
		return availabilityId;
	}

	public void setAvailabilityId(String availabilityId) {
		this.availabilityId = availabilityId;
	}

	public int getSelectedSlot() {
		return selectedSlot;
	}

	public void setSelectedSlot(int selectedSlot) {
		this.selectedSlot = selectedSlot;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public List<Integer> getAvailableSlots() {
		return availableSlots;
	}

	public String getMessage() {
		return message;
	}

	public boolean isSuccess() {
		return success;
	}
}
