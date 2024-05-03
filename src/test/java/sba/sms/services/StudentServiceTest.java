package sba.sms.services;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class StudentServiceTest {
    private static Session session;
    @BeforeAll
    static void initialize(){
        session = HibernateUtil.getSessionFactory().openSession();
    }
    @Test
    public void testCreateStudent(){
        session.beginTransaction();
        Student student = new Student("testuser@gmail.com", "testuser", "testpassword");
        session.persist(student);
        session.getTransaction().commit();
        assertTrue("testuser@gmail.com".equals(student.getEmail()));
    }
    @Test
    public void testgetStudentByEmail(){

        Student student = session.get(Student.class, "testuser@gmail.com");

        assertThat(student).isNotNull();

    }
    @Test
    public void testValidateStudent(){
        Student student = session.get(Student.class, "testuser@gmail.com");
        assertTrue("testuser@gmail.com".equals(student.getEmail()) && "testpassword".equals(student.getPassword()));
    }
    @Test
    public void testGetAllStudents(){

        List<Student> students = session.createQuery("from Student").list();

        assertFalse(students.isEmpty());
//        assertThat(students).isNotNull();
    }
    @Test
    public void testRegisterStudentToCourse(){
        session.beginTransaction();
        Student student = session.get(Student.class, "testuser@gmail.com");
        Course course = new Course("John", "Marketing");
        student.getCourses().add(course);
        session.merge(student);
        session.getTransaction().commit();
        assertFalse(student.getCourses().isEmpty());
    }
    @Test
    public void testGetStudentCourses(){
        Student student = session.get(Student.class, "testuser@gmail.com");
        assertFalse(student.getCourses().isEmpty());
    }
    @AfterAll
    static void close(){
        if (session != null) session.close();
    }

}