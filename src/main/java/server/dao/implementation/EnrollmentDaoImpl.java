package server.dao.implementation;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import server.dao.CourseDao;
import server.dao.EnrollmentDao;
import server.dao.StudentDao;
import server.domain.Course;
import server.domain.Enrollment;
import server.domain.Student;
import server.dto.EnrollmentDTO;

import java.util.*;

@Repository
public class EnrollmentDaoImpl implements EnrollmentDao {

    private final SessionFactory sessionFactory;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public EnrollmentDaoImpl(SessionFactory sessionFactory, StudentDao studentDao, CourseDao courseDao) {
        this.sessionFactory = sessionFactory;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    @Override
    public void addEnrollment(EnrollmentDTO enrollmentDTO) {
        Optional<Student> optionalStudent = studentDao.getStudentByUsername(enrollmentDTO.getUsername());
        Optional<Course> optionalCourse = courseDao.getCourseByTitle(enrollmentDTO.getTitle());
        Session session = sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        Enrollment enrollment = new Enrollment();
        optionalStudent.ifPresent(enrollment::setStudent);
        optionalCourse.ifPresent(enrollment::setCourse);
        session.save(enrollment);
        transaction.commit();
        session.clear();
        session.close();
    }

    @Override
    public List<Enrollment> getEnrollmentsByCriteria(String username, String title) {
        Session session = sessionFactory.openSession();
        List<Enrollment> enrollments = new ArrayList<>();
        Optional<Course> optionalCourse = courseDao.getCourseByTitle(title);
        Optional<Student> optionalStudent = studentDao.getStudentByUsername(username);
        try {
            if (title != null && username == null) {

                if (optionalCourse.isPresent()) {
                    Query query = session.createQuery(
                            "SELECT e.student FROM Enrollment e WHERE e.course = :course");
                    query.setParameter("course", optionalCourse.get());
                    enrollments = query.list();
                }
            } else if (username != null && title == null) {

                if (optionalStudent.isPresent()) {
                    Query query = session.createQuery(
                            "SELECT e.course FROM Enrollment e WHERE e.student = :student");
                    query.setParameter("student", optionalStudent.get());
                    enrollments = query.list();
                }
            } else if (title != null && username != null) {

                if (optionalStudent.isPresent() && optionalCourse.isPresent()) {
                    Query query = session.createQuery(
                            "FROM Enrollment e WHERE e.course = :course AND e.student = :student");
                    query.setParameter("course", optionalCourse.get());
                    query.setParameter("student", optionalStudent.get());
                    enrollments = query.list();
                }
            } else {
                Query query = session.createQuery("FROM Enrollment");
                enrollments = query.list();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        } finally {
            session.clear();
            session.close();
        }

        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourseTitle(String courseTitle) {
        Session session = sessionFactory.openSession();
        Optional<Course> course = courseDao.getCourseByTitle(courseTitle);

        if (course.isPresent()) {
            try {
                String hql = "FROM Enrollment e WHERE e.course = :course";
                Query query = session.createQuery(hql);
                query.setParameter("course", course.get());
                return query.list(); // Use list() instead of getResultList()
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception according to your application's needs
                return Collections.emptyList();
            } finally {
                session.clear();
                session.close();
            }
        }
        session.clear();
        session.close();
        return Collections.emptyList();
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentUserName(String username) {
        Session session = sessionFactory.openSession();
        Optional<Student> optionalStudent=studentDao.getStudentByUsername(username);

        if (optionalStudent.isPresent()) {
            try {
                String hql = "FROM Enrollment e WHERE e.student = :student";
                Query query = session.createQuery(hql);
                query.setParameter("student", optionalStudent.get());
                return query.list(); // Use list() instead of getResultList()
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception according to your application's needs
                return Collections.emptyList();
            } finally {
                session.clear();
                session.close();
            }
        }
        session.clear();
        session.close();
        return Collections.emptyList();
    }


    @Override
    public void deleteEnrollment(String userName, String title) {
        Optional<Student> student = studentDao.getStudentByUsername(userName);
        Optional<Course> course = courseDao.getCourseByTitle(title);
        Optional<Enrollment> enrollment= Optional.empty();
        if (student.isPresent() && course.isPresent()) {
             enrollment = getEnrollmentByStudentAndCourse(student.get(), course.get());

        }
        assert Objects.requireNonNull(enrollment).isPresent();
        enrollment.ifPresent(this::deleteEnrollmentFromSession);
    }

    private void deleteEnrollmentFromSession(Enrollment enrollment) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(enrollment);
            transaction.commit();
            session.clear();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception according to your application's needs
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Enrollment> getEnrollmentByStudentAndCourse(Student student, Course course) {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery(
                    "FROM Enrollment e WHERE e.student = :student AND e.course = :course");
            query.setParameter("student", student);
            query.setParameter("course", course);

            Enrollment enrollment = (Enrollment) query.uniqueResult();
            return Optional.ofNullable(enrollment);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            session.clear();
            session.close();
        }
    }

}
