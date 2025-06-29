package com.infinite.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.infinite.dao.AppointmentDaoImpl;
import com.infinite.dao.DoctorAvailabilityDaoImpl;
import com.infinite.dao.DoctorDaoImpl;
import com.infinite.dao.ProviderDaoImpl;
import com.infinite.dao.RecipientDaoImpl;
import com.infinite.model.Appointment;
import com.infinite.model.AppointmentStatus;
import com.infinite.model.DoctorAvailability;
import com.infinite.model.Doctors;
import com.infinite.model.Providers;
import com.infinite.model.Recipient;
import com.infinite.util.SessionHelper;

import org.hibernate.Query;
import org.hibernate.Session;

public class DoctorProviderController implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient DoctorDaoImpl doctorDao = new DoctorDaoImpl();
    private transient ProviderDaoImpl providerDao = new ProviderDaoImpl();
    private transient RecipientDaoImpl recipientDao = new RecipientDaoImpl();
    private transient DoctorAvailabilityDaoImpl availabilityDao = new DoctorAvailabilityDaoImpl();
    private transient AppointmentDaoImpl appointmentDao = new AppointmentDaoImpl();

    private Doctors doctor;
    private Providers provider;
    private Recipient recipient;
    private DoctorAvailability availability;
    private Appointment appointment = new Appointment();

    private String doctorId;
    private String providerId;
    private String customDate;
    private String notes;

    public DoctorProviderController() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        doctorId = (String) session.getAttribute("selectedDoctorId");
        providerId = (String) session.getAttribute("selectedProviderId");

        loadEntities();
    }

    private void loadEntities() {
        if (doctorId != null && !doctorId.isEmpty()) {
            doctor = doctorDao.searchADoctorById(doctorId);
        }
        if (providerId != null && !providerId.isEmpty()) {
            provider = providerDao.searchProviderById(providerId);
        }
        recipient = recipientDao.searchRecipientById("H1005"); // Hardcoded recipient, can be dynamic
        availability = availabilityDao.searchDoctorAvailabilityByDoctorId(doctorId);
    }

    public String bookAppointment() {
        try {
            if (customDate == null || customDate.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Appointment date is required.", null));
                return null;
            }

            if (availability == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No availability found for the doctor.", null));
                return null;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdf.parse(customDate);
            Timestamp bookedAt = new Timestamp(parsedDate.getTime());

            // Count existing appointments for this availability
            int existingAppointments = getCurrentAppointmentsCount(availability.getAvailability_id());

            if (existingAppointments >= availability.getMax_capacity()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "All slots are already booked.", null));
                return null;
            }

            int slotNo = existingAppointments + 1;

            // Set appointment fields
            appointment.setDoctor(doctor);
            appointment.setProvider(provider);
            appointment.setRecipient(recipient);
            appointment.setAvailability(availability);
            appointment.setBooked_at(bookedAt);
            appointment.setRequested_at(new Timestamp(System.currentTimeMillis()));
            appointment.setStatus(AppointmentStatus.PENDING);
            appointment.setNotes(notes);
            appointment.setSlot_no(slotNo);

            String result = appointmentDao.bookAnAppointment(appointment);

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

            if (result.startsWith("Appointment Booked")) {
                session.setAttribute("confirmationMessage", "✅ " + result);
                return "appointmentConfirmation?faces-redirect=true";
            } else {
                session.setAttribute("confirmationMessage", "⚠️ " + result);
                return "index?faces-redirect=true";
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while booking appointment.", null));
            return null;
        }
    }

    private int getCurrentAppointmentsCount(String availabilityId) {
        int count = 0;
        try {Session session = SessionHelper.getSessionFactory().openSession();
        Query query = session.createQuery(
        	    "SELECT COUNT(a) FROM Appointment a WHERE a.availability.availability_id = :aid");
        	query.setParameter("aid", availabilityId);
        	Long result = (Long) query.uniqueResult();
            if (result != null) count = result.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // Getters and Setters
    public Doctors getDoctor() {
        return doctor;
    }

    public Providers getProvider() {
        return provider;
    }

    public String getCustomDate() {
        return customDate;
    }

    public void setCustomDate(String customDate) {
        this.customDate = customDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
