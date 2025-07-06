package com.infinite.main;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import com.infinite.dao.AppointmentDao;
import com.infinite.dao.AppointmentDaoImpl;
import com.infinite.model.Appointment;
import com.infinite.model.AppointmentStatus;
import com.infinite.model.DoctorAvailability;
import com.infinite.model.Doctors;
import com.infinite.model.Providers;
import com.infinite.model.Recipient;

public class Appoint {

    public static void main(String[] args) {

        // Create Appointment object
        Appointment appointment = new Appointment();
        appointment.setAppointment_id(UUID.randomUUID().toString());

        // Set doctor info
        Doctors doctor = new Doctors();
        doctor.setDoctor_id("D1003"); // must exist in DB
        appointment.setDoctor(doctor);

        // Set provider info
        Providers provider = new Providers();
        provider.setProvider_id("P1003"); // must exist in DB
        appointment.setProvider(provider);

        // Set recipient info
        Recipient recipient = new Recipient();
        recipient.setH_id("H1003"); // must exist in DB
        appointment.setRecipient(recipient);

        // Set availability info
        DoctorAvailability availability = new DoctorAvailability();
        availability.setAvailability_id("A2012"); // must be valid and future dated
        availability.setMax_capacity(4); // set this so that validation doesn't fail
        appointment.setAvailability(availability);

        // Set desired slot number
        appointment.setSlot_no(2); // Slot number should be between 1 and max_capacity

        // Calculate slot start/end based on availability and slot number
        Timestamp availabilityStart = Timestamp.valueOf("2025-07-09 10:30:00");
        Timestamp availabilityEnd = Timestamp.valueOf("2025-07-09 11:30:00");

        int maxCapacity = availability.getMax_capacity();
        long windowMinutes = (availabilityEnd.getTime() - availabilityStart.getTime()) / (1000 * 60) / maxCapacity;

        long slotStartMillis = availabilityStart.getTime() + (appointment.getSlot_no() - 1) * windowMinutes * 60 * 1000;
        long slotEndMillis = slotStartMillis + windowMinutes * 60 * 1000;

        appointment.setStart(new Timestamp(slotStartMillis));
        appointment.setEnd(new Timestamp(slotEndMillis));

        // Set booking timestamps
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        appointment.setRequested_at(now);
        appointment.setBooked_at(now);

        // Status and Notes
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setNotes("Test appointment from main");

        // Call the DAO
        AppointmentDao dao = new AppointmentDaoImpl();
        String result = dao.bookAnAppointment(appointment);

        // Show result
        if (result != null && result.startsWith("Appointment booked")) {
            System.out.println("✅ " + result);
        } else {
            System.out.println("❌ Booking failed: " + result);
        }
    }
}
