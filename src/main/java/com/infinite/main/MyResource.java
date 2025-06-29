package com.infinite.main;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.infinite.dao.AppointmentDaoImpl;
import com.infinite.dao.DoctorAvailabilityDaoImpl;
import com.infinite.model.Appointment;
import com.infinite.model.AppointmentStatus;
import com.infinite.model.DoctorAvailability;
import com.infinite.model.Doctors;
import com.infinite.model.Providers;
import com.infinite.model.Recipient;

public class MyResource {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Sample IDs (assumed to exist in DB)
		String doctorId = "D1009";
		String recipientId = "H1005";
		String providerId = "P1001";

		// Take user input for appointment booked date
		System.out.print("Enter appointment booked date (yyyy-MM-dd): ");
		String inputDateStr = scanner.nextLine();

		Timestamp bookedAtTimestamp = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date parsedDate = sdf.parse(inputDateStr);
			bookedAtTimestamp = new Timestamp(parsedDate.getTime()); // time will be 00:00:00
		} catch (Exception e) {
			System.out.println("❌ Invalid date format. Please use yyyy-MM-dd");
			scanner.close();
			return;
		}

		// Create Doctor object
		Doctors doctor = new Doctors();
		doctor.setDoctor_id(doctorId);

		// Create Recipient object
		Recipient recipient = new Recipient();
		recipient.setH_id(recipientId);

		// Create Provider object
		Providers provider = new Providers();
		provider.setProvider_id(providerId);

		// 🔍 Search DoctorAvailability by doctor ID
		DoctorAvailabilityDaoImpl availabilityDao = new DoctorAvailabilityDaoImpl();
		DoctorAvailability availability = availabilityDao.searchDoctorAvailabilityByDoctorId(doctorId);

		// ❗ Check for null
		if (availability == null) {
			System.out.println("❌ No DoctorAvailability found for doctorId: " + doctorId);
			scanner.close();
			return;
		}

		// ✅ Prepare Appointment
		Appointment appointment = new Appointment();
		appointment.setDoctor(doctor);
		appointment.setRecipient(recipient);
		appointment.setProvider(provider);
		appointment.setAvailability(availability);
		appointment.setRequested_at(new Timestamp(new Date().getTime())); // system current
		appointment.setBooked_at(bookedAtTimestamp); // user input date only
		appointment.setStatus(AppointmentStatus.PENDING);

		// 💾 Book Appointment
		AppointmentDaoImpl dao = new AppointmentDaoImpl();
		String result = dao.bookAnAppointment(appointment);

		// ✅ Print result
		System.out.println(result);
		scanner.close();
	}
}
