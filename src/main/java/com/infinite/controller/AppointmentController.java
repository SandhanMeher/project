package com.infinite.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.infinite.dao.AppointmentDao;
import com.infinite.dao.AppointmentDaoImpl;
import com.infinite.dao.DoctorAvailabilityDao;
import com.infinite.dao.DoctorAvailabilityDaoImpl;
import com.infinite.model.*;

@ManagedBean(name = "appointmentController")
@SessionScoped
public class AppointmentController implements Serializable {

	private static final long serialVersionUID = 1L;

	private String availabilityId;
	private int selectedSlot;
	private Appointment appointment = new Appointment();
	private DoctorAvailability availability;
	private List<SlotDisplay> availableSlots;

	private String message;
	private boolean success;

	private AppointmentDao appointmentDao = new AppointmentDaoImpl();
	private DoctorAvailabilityDao doctorAvailabilityDao = new DoctorAvailabilityDaoImpl();

	// Called from UI to load slots
	public String selectAvailabilityAndLoadSlots(String selectedAvailabilityId) {
		this.availabilityId = selectedAvailabilityId;
		return loadAvailableSlots();
	}

	public String loadAvailableSlots() {
		try {
			availableSlots = new ArrayList<>();

			if (availabilityId == null || availabilityId.trim().isEmpty()) {
				message = "❌ Availability ID is missing.";
				success = false;
				return null;
			}

			availability = doctorAvailabilityDao.getAvailabilityById(availabilityId);
			if (availability == null) {
				message = "❌ Availability not found for ID: " + availabilityId;
				success = false;
				return null;
			}

			List<Integer> slotNumbers = appointmentDao.getAvailableSlotNumbers(availabilityId);
			if (slotNumbers == null || slotNumbers.isEmpty()) {
				message = "❌ No available slots found.";
				success = false;
				return null;
			}

			Timestamp start = Timestamp.valueOf(availability.getAvailable_date() + " " + availability.getStart_time());
			Timestamp end = Timestamp.valueOf(availability.getAvailable_date() + " " + availability.getEnd_time());
			int maxCapacity = availability.getMax_capacity();

			long totalMinutes = (end.getTime() - start.getTime()) / (1000 * 60);
			long slotMinutes = totalMinutes / maxCapacity;

			for (int slotNo : slotNumbers) {
				long slotStartMillis = start.getTime() + (slotNo - 1) * slotMinutes * 60 * 1000;
				long slotEndMillis = slotStartMillis + slotMinutes * 60 * 1000;

				LocalTime slotStartTime = new Timestamp(slotStartMillis).toLocalDateTime().toLocalTime();
				LocalTime slotEndTime = new Timestamp(slotEndMillis).toLocalDateTime().toLocalTime();

				availableSlots.add(new SlotDisplay(slotNo, slotStartTime.toString(), slotEndTime.toString()));
			}

			message = "✅ Loaded " + availableSlots.size() + " available slot(s)";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "❌ Error loading slots: " + e.getMessage();
			success = false;
			availableSlots = new ArrayList<>();
		}
		return "slotListView"; // navigate to slot listing page if needed
	}

	public String bookAppointment() {
		try {
			if (availability == null) {
				availability = doctorAvailabilityDao.getAvailabilityById(availabilityId);
			}
			if (availability == null) {
				message = "❌ Availability not found.";
				success = false;
				return null;
			}

			Doctors doctor = availability.getDoctor();
			Providers provider = doctor.getProvider();

			Recipient recipient = new Recipient();
			recipient.setH_id("H1003"); // should be dynamic from logged in user

			Timestamp start = Timestamp.valueOf(availability.getAvailable_date() + " " + availability.getStart_time());
			Timestamp end = Timestamp.valueOf(availability.getAvailable_date() + " " + availability.getEnd_time());
			int maxCapacity = availability.getMax_capacity();

			long slotMinutes = ((end.getTime() - start.getTime()) / (1000 * 60)) / maxCapacity;
			long slotStart = start.getTime() + (selectedSlot - 1) * slotMinutes * 60 * 1000;
			long slotEnd = slotStart + slotMinutes * 60 * 1000;

			appointment.setAvailability(availability);
			appointment.setDoctor(doctor);
			appointment.setProvider(provider);
			appointment.setRecipient(recipient);
			appointment.setSlot_no(selectedSlot);
			appointment.setStart(new Timestamp(slotStart));
			appointment.setEnd(new Timestamp(slotEnd));
			appointment.setRequested_at(Timestamp.valueOf(LocalDateTime.now()));
			appointment.setBooked_at(Timestamp.valueOf(LocalDateTime.now()));
			appointment.setStatus(AppointmentStatus.BOOKED);
			appointment.setNotes("Booked via Controller");

			String result = appointmentDao.bookAnAppointment(appointment);

			if (result.startsWith("Appointment booked")) {
				message = "✅ " + result;
				success = true;
				loadAvailableSlots();
			} else {
				message = "❌ Booking failed: " + result;
				success = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "❌ Internal error: " + e.getMessage();
		}
		return null;
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

	public List<SlotDisplay> getAvailableSlots() {
		return availableSlots;
	}

	public String getMessage() {
		return message;
	}

	public boolean isSuccess() {
		return success;
	}

	public DoctorAvailability getAvailability() {
		return availability;
	}
}
