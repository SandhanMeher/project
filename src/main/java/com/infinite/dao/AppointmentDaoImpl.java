package com.infinite.dao;

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

	    try {Session session = SessionHelper.getSessionFactory().openSession();
	        tx = session.beginTransaction();

	        // Step 1: Get Doctor ID from appointment
	        String doctorId = appointment.getDoctor().getDoctor_id();

	        // Step 2: Fetch DoctorAvailability by doctorId
	        Query query = session.createQuery("from DoctorAvailability where doctor.doctor_id = :doctorId");
	        query.setParameter("doctorId", doctorId);
	        DoctorAvailability availability = (DoctorAvailability) query.uniqueResult();

	        if (availability == null) {
	            return "Booking Failed: No availability found for doctor with ID: " + doctorId;
	        }

	        // Step 3: Set the fetched availability in appointment
	        appointment.setAvailability(availability);

	        // Step 4: Check how many appointments are already booked for this availability
	        Long currentCount = (Long) session.createQuery(
	                "SELECT COUNT(a) FROM Appointment a WHERE a.availability.availability_id = :availId")
	                .setParameter("availId", availability.getAvailability_id())
	                .uniqueResult();

	        // Step 5: Check capacity
	        if (currentCount >= availability.getMax_capacity()) {
	            return "Booking Failed: Slot full for selected doctor.";
	        }

	        // Step 6: Generate new appointment_id
	        String prefix = "AP";
	        String latestId = (String) session.createQuery("SELECT MAX(a.appointment_id) FROM Appointment a")
	                .uniqueResult();

	        int nextId = 1;
	        if (latestId != null && latestId.startsWith(prefix)) {
	            nextId = Integer.parseInt(latestId.substring(2)) + 1;
	        }
	        appointmentId = prefix + nextId;
	        appointment.setAppointment_id(appointmentId);

	        // Step 7: Assign slot number (currentCount + 1)
	        appointment.setSlot_no((int) (currentCount + 1));

	        // Step 8: Save appointment
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
