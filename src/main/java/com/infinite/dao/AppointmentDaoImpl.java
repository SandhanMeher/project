package com.infinite.dao;

import java.math.BigInteger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.infinite.model.Appointment;
import com.infinite.model.DoctorAvailability;
import com.infinite.util.SessionHelper;

public class AppointmentDaoImpl implements AppointmentDao {

	@Override
	public String bookAnAppointment(Appointment appointment) {
		Transaction tx = null;
		String appointmentId = null;

		try {
			Session session = SessionHelper.getSessionFactory().openSession();
			tx = session.beginTransaction();

			// Step 1: Get selected availability from appointment
			DoctorAvailability availability = appointment.getAvailability();
			if (availability == null || availability.getAvailability_id() == null) {
				return "Booking Failed: No availability selected.";
			}

			// Step 2: Reload availability from DB
			DoctorAvailability dbAvailability = (DoctorAvailability) session.get(DoctorAvailability.class,
					availability.getAvailability_id());
			if (dbAvailability == null) {
				return "Booking Failed: Selected availability not found.";
			}

			// Step 3: Check current appointment count
			Long currentCount = (Long) session
					.createQuery("SELECT COUNT(a) FROM Appointment a WHERE a.availability.availability_id = :availId")
					.setParameter("availId", dbAvailability.getAvailability_id()).uniqueResult();
			if (currentCount == null)
				currentCount = 0L;

			// Step 4: Check max capacity
			if (currentCount >= dbAvailability.getMax_capacity()) {
				return "Booking Failed: Slot full for selected time.";
			}

			// Step 5: Generate appointment ID
			String prefix = "AP";
			String sql = "SELECT MAX(CAST(SUBSTRING(appointment_id, 3) AS UNSIGNED)) FROM appointment WHERE appointment_id LIKE 'AP%'";
			BigInteger maxNum = (BigInteger) session.createSQLQuery(sql).uniqueResult();
			int nextId = (maxNum != null) ? maxNum.intValue() + 1 : 1;
			appointmentId = prefix + nextId;
			appointment.setAppointment_id(appointmentId); // ✅ Correct and only once

			// Step 6: Assign slot number
			appointment.setSlot_no((int) (currentCount + 1));

			// Step 7: Link availability
			appointment.setAvailability(dbAvailability);

			// Step 8: Save
			session.save(appointment);
			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return "Booking Failed: " + e.getMessage();
		}

		return "Appointment Booked with ID: " + appointmentId;
	}


	@Override
	public boolean isAvailabilitySlotFull(String availabilityId) {
		try {
			Session session = SessionHelper.getSessionFactory().openSession();
			// Step 1: Get max capacity for availability
			Query availabilityQuery = session
					.createQuery("SELECT a.max_capacity FROM DoctorAvailability a WHERE a.availability_id = :aid");
			availabilityQuery.setParameter("aid", availabilityId);
			Integer maxCapacity = (Integer) availabilityQuery.uniqueResult();

			if (maxCapacity == null)
				return true;

			// Step 2: Get current count
			Query countQuery = session
					.createQuery("SELECT COUNT(a) FROM Appointment a WHERE a.availability.availability_id = :aid");
			countQuery.setParameter("aid", availabilityId);
			Long currentCount = (Long) countQuery.uniqueResult();

			if (currentCount == null)
				currentCount = 0L;

			return currentCount >= maxCapacity;
		} catch (Exception e) {
			e.printStackTrace();
			return true; // safely assume full
		}
	}
}
