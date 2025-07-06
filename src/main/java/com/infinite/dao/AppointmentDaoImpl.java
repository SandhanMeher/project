package com.infinite.dao;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.infinite.model.Appointment;
import com.infinite.model.AppointmentStatus;
import com.infinite.model.DoctorAvailability;
import com.infinite.util.SessionHelper;

public class AppointmentDaoImpl implements AppointmentDao {

	// Generates the next appointment ID in APPT### format
	private String generateNextAppointmentId(Session session) {
	    String prefix = "APPT";
	    String hql = "SELECT a.appointment_id FROM Appointment a ORDER BY a.appointment_id DESC";
	    Query query = session.createQuery(hql);
	    query.setMaxResults(1);
	    String lastId = (String) query.uniqueResult();

	    int nextNumber = 101; // default start
	    if (lastId != null && lastId.startsWith(prefix)) {
	        try {
	            int lastNumber = Integer.parseInt(lastId.substring(prefix.length()));
	            nextNumber = lastNumber + 1;
	        } catch (NumberFormatException e) {
	            // fallback to default 101
	        }
	    }

	    return prefix + nextNumber;
	}

	@Override
	public String bookAnAppointment(Appointment appointment) {
		Transaction tx = null;
		String result = null;

		try {
			Session session = SessionHelper.getSessionFactory().openSession();
			tx = session.beginTransaction();

			String availabilityId = appointment.getAvailability().getAvailability_id();
			String recipientId = appointment.getRecipient().getH_id();
			int slotNo = appointment.getSlot_no();
			Timestamp now = new Timestamp(System.currentTimeMillis());

			// 1. Prevent booking in the past
			Timestamp slotStart = appointment.getStart();
			if (slotStart == null || slotStart.before(now)) {
				return "Cannot book an appointment in the past.";
			}

			// 2. Check if recipient already has overlapping appointment
			Query overlapQuery = session.createQuery(
					"FROM Appointment a WHERE a.recipient.h_id = :recipientId AND a.status IN ('BOOKED', 'PENDING') "
							+ "AND (a.start <= :endTime AND a.end >= :startTime)");
			overlapQuery.setParameter("recipientId", recipientId);
			overlapQuery.setParameter("startTime", appointment.getStart());
			overlapQuery.setParameter("endTime", appointment.getEnd());

			if (!overlapQuery.list().isEmpty()) {
				return "Recipient already has an overlapping appointment during this time.";
			}

			// 3. Check if the slot number is already booked in this availability
			Query slotQuery = session
					.createQuery("FROM Appointment WHERE availability.availability_id = :availabilityId "
							+ "AND slot_no = :slotNo AND status IN ('BOOKED', 'PENDING')");
			slotQuery.setParameter("availabilityId", availabilityId);
			slotQuery.setParameter("slotNo", slotNo);

			if (!slotQuery.list().isEmpty()) {
				return "Slot number " + slotNo + " is already booked for this availability.";
			}

			// 4. Check if max capacity is reached for this availability
			Query bookedCountQuery = session.createQuery(
					"SELECT COUNT(*) FROM Appointment WHERE availability.availability_id = :availabilityId "
							+ "AND status IN ('BOOKED', 'PENDING')");
			bookedCountQuery.setParameter("availabilityId", availabilityId);
			long bookedCount = (Long) bookedCountQuery.uniqueResult();

			int maxCapacity = appointment.getAvailability().getMax_capacity();
			if (bookedCount >= maxCapacity) {
				return "All slots for this availability are already full.";
			}

			// 5. Check if recipient already has 10 upcoming appointments
			Query upcomingQuery = session
					.createQuery("SELECT COUNT(*) FROM Appointment WHERE recipient.h_id = :recipientId "
							+ "AND status IN ('BOOKED', 'PENDING') AND start > :now");
			upcomingQuery.setParameter("recipientId", recipientId);
			upcomingQuery.setParameter("now", now);

			long upcomingCount = (Long) upcomingQuery.uniqueResult();
			if (upcomingCount >= 10) {
				return "Recipient already has 10 upcoming appointments.";
			}

			// 6. Save the appointment
			appointment.setRequested_at(now);
			appointment.setBooked_at(now);
			appointment.setStatus(AppointmentStatus.BOOKED);
			appointment.setAppointment_id(generateNextAppointmentId(session));

			session.save(appointment);
			tx.commit();

			// Placeholder: Send email, generate PDF, send OTP
			// EmailService.sendAppointmentEmail(appointment);
			// PdfService.generateAppointmentSlip(appointment);
			// OtpService.sendOtpToRecipient(appointment.getRecipient().getMobile());

			result = "Appointment booked successfully with ID: " + appointment.getAppointment_id();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			result = "Error booking appointment: " + e.getMessage();
		}

		return result;
	}

	@Override
	public boolean isAvailabilitySlotFull(String availabilityId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Appointment> getUpcomingAppointmentsByRecipient(String recipientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Appointment> getPastAppointmentsByRecipient(String recipientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appointment getAppointmentById(String appointmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean cancelAppointment(String appointmentId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateAppointment(Appointment appointment) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBookedCountForAvailability(String availabilityId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasOverlappingAppointment(String recipientId, String availabilityId, Timestamp start,
			Timestamp end) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Integer> getAvailableSlotNumbers(String availabilityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSlotAlreadyBooked(String availabilityId, int slotNo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Appointment> getAppointmentsByAvailability(String availabilityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAppointmentInPast(String appointmentId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Appointment> getAppointmentsByDoctorAndDate(String doctorId, Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSlotTimeInFuture(String availabilityId, int slotNo) {
		// TODO Auto-generated method stub
		return false;
	}

}
