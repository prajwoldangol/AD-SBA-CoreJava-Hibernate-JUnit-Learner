package sba.sms.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI {
    Transaction transaction = null;
    Session session = null;

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            students = session.createQuery("from Student").list();

        } catch (Exception e) {
            System.out.println(" Operation Failed  to get all students!!!");
        } finally {
            if (session != null) session.close();
        }
        return students;
    }

    @Override
    public void createStudent(Student student) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(" Operation Failed while creating a new student !!!");
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        Student student = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            student = session.get(Student.class, email);
        } catch (Exception e) {
            System.out.println(" Operation Failed  to get student by email : !!!");
        } finally {
            if (session != null) session.close();
        }
        return student;
    }

    @Override
    public boolean validateStudent(String email, String password) {
        Student student = getStudentByEmail(email);
        if (student == null) return false;
        if (student.getPassword().equals(password)) return true;
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Set<Course> courseList;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, email);
            Course course = session.get(Course.class, courseId);
            courseList = student.getCourses();
            if (!courseList.contains(course)) {
                courseList.add(course);
                student.setCourses(courseList);
                session.merge(student);
                transaction.commit();
            } else {
                System.out.println(" Student Already Registered with Course " + course.getName());
            }

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println(" Operation Failed while registering !!!");
        } finally {
            if (session != null) session.close();
        }

    }

    @Override
    public List<Course> getStudentCourses(String email) {
        List<Course> courseList = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
//            Student student = session.createQuery("SELECT s from Student s where s.email=:email", Student.class).setParameter("email", email).getSingleResult();
            Student student = session.get(Student.class, email);
            if (student.getCourses() != null) {
                courseList.addAll(student.getCourses());
            }

        } catch (Exception e) {
            System.out.println(" Operation Failed to get student courses : !!!");
        } finally {
            if (session != null) session.close();
        }
        return courseList;
    }
}
