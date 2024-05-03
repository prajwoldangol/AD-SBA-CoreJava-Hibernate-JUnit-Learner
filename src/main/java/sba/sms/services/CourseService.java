package sba.sms.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI {
    Transaction transaction = null;
    Session session = null;

    @Override
    public void createCourse(Course course) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(" Operation Failed to create course!!!");
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        Course course = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            course = session.get(Course.class, courseId);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(" Operation Failed to get by course id !!!");
        } finally {
            if (session != null) session.close();
        }
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            courses = session.createQuery("from Course").list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(" Operation Failed while getting all courses!!!");
        } finally {
            if (session != null) session.close();
        }
        return courses;
    }
}
