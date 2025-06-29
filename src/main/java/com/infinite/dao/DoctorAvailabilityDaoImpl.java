package com.infinite.dao;

import java.sql.Date;
import java.util.List;

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
    public List<DoctorAvailability> getAvailableSlotsByDoctorAndDate(String doctorId, Date date) {
        Session session = null;
        List<DoctorAvailability> slots = null;

        try {
            session = sessionFactory.openSession();
            Query query = session.createQuery(
                "from DoctorAvailability where doctor.doctor_id = :doctorId and available_date = :date"
            );
            query.setParameter("doctorId", doctorId);
            query.setParameter("date", date);
            slots = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }

        return slots;
    }

    @Override
    public DoctorAvailability getAvailabilityById(String availabilityId) {
        Session session = null;
        DoctorAvailability availability = null;

        try {
            session = sessionFactory.openSession();
            availability = (DoctorAvailability) session.get(DoctorAvailability.class, availabilityId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }

        return availability;
    }
}
