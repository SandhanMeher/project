package com.infinite.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.infinite.model.DoctorAvailability;
import com.infinite.util.SessionHelper;

public class DoctorAvailabilityDaoImpl implements DoctorAvailabilityDao {

	private SessionFactory sessionFactory;

	public DoctorAvailabilityDaoImpl() {
		this.sessionFactory = SessionHelper.getSessionFactory();
	}

	@Override
	public DoctorAvailability searchDoctorAvailabilityByDoctorId(String doctorId) {
		Session session = null;
		DoctorAvailability availability = null;

		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery("from DoctorAvailability where doctor.doctor_id = :doctorId");
			query.setParameter("doctorId", doctorId);
			availability = (DoctorAvailability) query.uniqueResult(); // returns single row or null
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}

		return availability;
	}
}
