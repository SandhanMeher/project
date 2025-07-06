package com.infinite.controller;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.infinite.dao.DoctorAvailabilityDaoImpl;
import com.infinite.dto.GroupedAvailability;
import com.infinite.model.DoctorAvailability;

@ManagedBean
@ViewScoped
public class DoctorAvailabilityController implements Serializable {

	private static final long serialVersionUID = 1L;

	private DoctorAvailabilityDaoImpl dao = new DoctorAvailabilityDaoImpl();

	private String doctorId = "D1003"; // hardcoded for demo
	private List<GroupedAvailability> groupedAvailabilityList;
	private List<DoctorAvailability> searchedAvailabilityList;
	private DoctorAvailability selectedAvailability;
	private java.util.Date searchDate;
	private boolean initialized = false;

	public void init() {
		if (initialized)
			return;
		initialized = true;
		loadNextFiveDaysAvailability();
	}

	public void loadNextFiveDaysAvailability() {
		groupedAvailabilityList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();

		for (int i = 0; i < 5; i++) {
			Date sqlDate = new Date(calendar.getTimeInMillis());
			List<DoctorAvailability> dailySlots = dao.getAvailableSlotsByDoctorAndDate(doctorId, sqlDate);
			if (dailySlots != null && !dailySlots.isEmpty()) {
				groupedAvailabilityList.add(new GroupedAvailability(sqlDate, dailySlots));
			}
			calendar.add(Calendar.DATE, 1);
		}
	}

	public void searchAvailabilityByDate() {
		searchedAvailabilityList = new ArrayList<>();
		if (searchDate != null) {
			Date sqlDate = new Date(searchDate.getTime());
			searchedAvailabilityList = dao.getAvailableSlotsByDoctorAndDate(doctorId, sqlDate);
		}
	}

	public void selectAvailability(DoctorAvailability availability) {
		this.selectedAvailability = availability;
	}

	// Getters and Setters
	public String getDoctorId() {
		return doctorId;
	}

	public List<GroupedAvailability> getGroupedAvailabilityList() {
		init();
		return groupedAvailabilityList;
	}

	public List<DoctorAvailability> getSearchedAvailabilityList() {
		return searchedAvailabilityList;
	}

	public java.util.Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(java.util.Date searchDate) {
		this.searchDate = searchDate;
	}

	public DoctorAvailability getSelectedAvailability() {
		return selectedAvailability;
	}

	public void setSelectedAvailability(DoctorAvailability selectedAvailability) {
		this.selectedAvailability = selectedAvailability;
	}
}