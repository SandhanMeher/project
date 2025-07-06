package com.infinite.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.infinite.dao.DoctorAvailabilityDaoImpl;
import com.infinite.model.DoctorAvailability;

public class DoctorAvailabilityController implements Serializable {

	private static final long serialVersionUID = 1L;

	private DoctorAvailabilityDaoImpl dao = new DoctorAvailabilityDaoImpl();

	private String doctorId = "D1003"; // Hardcoded for testing
	private List<DoctorAvailability> futureAvailabilityList;
	private List<DoctorAvailability> searchedAvailabilityList;

	private Date searchDate;

	private boolean initialized = false;

	public void init() {
		if (initialized)
			return;
		initialized = true;

		loadNextFiveDaysAvailability();
	}

	public void loadNextFiveDaysAvailability() {
		futureAvailabilityList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 5; i++) {
			Date date = cal.getTime();
			List<DoctorAvailability> daySlots = dao.getAvailableSlotsByDoctorAndDate(doctorId,
					new java.sql.Date(date.getTime()));
			futureAvailabilityList.addAll(daySlots);
			cal.add(Calendar.DATE, 1);
		}
	}

	public void searchAvailabilityByDate() {
		searchedAvailabilityList = new ArrayList<>();
		if (searchDate != null) {
			java.sql.Date sqlDate = new java.sql.Date(searchDate.getTime());
			searchedAvailabilityList = dao.getAvailableSlotsByDoctorAndDate(doctorId, sqlDate);
		}
	}

	// Getters and Setters

	public List<DoctorAvailability> getFutureAvailabilityList() {
		init();
		return futureAvailabilityList;
	}

	public List<DoctorAvailability> getSearchedAvailabilityList() {
		return searchedAvailabilityList;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getDoctorId() {
		return doctorId;
	}
}
