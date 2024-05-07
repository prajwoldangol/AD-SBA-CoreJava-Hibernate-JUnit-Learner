package sba.sms.services;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseServiceTest {
    private static Session session;
    @BeforeAll
    static void initialize(){
        session = HibernateUtil.getSessionFactory().openSession();
    }
    @BeforeEach
    void setUp() {
        session.beginTransaction();
        Course course = new Course("Johns", "Marketing");
        session.persist(course);
        session.getTransaction().commit();
    }
    @Test
    public void testCreateCourse() {
        session.beginTransaction();
        Course course = new Course("Jeffery", "IT");
        session.persist(course);
        Course courseGet = session.get(Course.class, 4);
        assertTrue(courseGet.equals(course));
    }

    @Test
    public void testGetCourseById() {
        Course course = session.get(Course.class, 1);
        assertThat(course).isNotNull();
    }

    @Test
    public void testGetAllCourses() {
        List<Course> courseList = session.createQuery("from Course").list();
        assertFalse(courseList.isEmpty());
    }
    @AfterAll
    static void close(){
        if (session != null) session.close();
    }
}
