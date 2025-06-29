package com.infinite.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.infinite.model.Appointment;
import com.infinite.util.SessionHelper;

public class AppointmentDaoImpl implements AppointmentDao {

	@Override
	public String bookAnAppointment(Appointment appointment) {
		Transaction tx = null;
		String appointmentId = null;

		try {
			Session session = SessionHelper.getSessionFactory().openSession();
			tx = session.beginTransaction();

			// Generate a new appointment_id (e.g., "AP1001", "AP1002", etc.)
			String prefix = "AP";
			String latestId = (String) session.createQuery("SELECT MAX(a.appointment_id) FROM Appointment a")
					.uniqueResult();

			int nextId = 1;
			if (latestId != null && latestId.startsWith(prefix)) {
				nextId = Integer.parseInt(latestId.substring(2)) + 1;
			}
			appointmentId = prefix + nextId;
			appointment.setAppointment_id(appointmentId);

			// Save appointment
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
}
