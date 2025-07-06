package com.infinite.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.infinite.dao.DoctorAvailabilityDaoImpl;
import com.infinite.model.DoctorAvailability;

public class DoctorAvailabilityController implements Serializable {

	private static final long serialVersionUID = 1L;

	private DoctorAvailabilityDaoImpl dao = new DoctorAvailabilityDaoImpl();

	private List<DoctorAvailability> availabilityList;
	private String doctorId;

	private Date selectedDate;
	private boolean initialized = false;

	public void init() {
		if (initialized)
			return;
		initialized = true;

		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);

			// For isolated testing
			doctorId = "D1003";
			// doctorId = (String) session.getAttribute("selectedDoctorId");

			if (doctorId != null) {
				availabilityList = dao.getUpcomingAvailabilitiesForDoctor(doctorId,
						new java.sql.Date(System.currentTimeMillis()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadAvailabilityByDate() {
		try {
			if (doctorId != null && selectedDate != null) {
				java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
				availabilityList = dao.getAvailableSlotsByDoctorAndDate(doctorId, sqlDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ========================
	// Getters and Setters
	// ========================

	public List<DoctorAvailability> getAvailabilityList() {
		init();
		return availabilityList;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
}
