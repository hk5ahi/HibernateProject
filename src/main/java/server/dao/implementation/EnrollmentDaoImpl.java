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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Enrollment enrollment = new Enrollment();
        Optional<Student> optionalStudent = studentDao.getStudentByUsername(enrollmentDTO.getUsername());
        optionalStudent.ifPresent(enrollment::setStudent);

        Optional<Course> optionalCourse = courseDao.getCourseByTitle(enrollmentDTO.getTitle());
        optionalCourse.ifPresent(enrollment::setCourse);
        session.save(enrollment);

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Enrollment> getEnrollmentsByCriteria(String username, String title) {
        Session session = sessionFactory.openSession();

        if (title != null && username == null) {
            // Fetch students enrolled in a specific course by title
            Optional<Course> course = courseDao.getCourseByTitle(title);
            if (course.isPresent()) {
                Query query = session.createQuery(
                        "SELECT e.student FROM Enrollment e WHERE e.course = :course");
                query.setParameter("course", course.get());
                return query.list();
            }
        } else if (username != null && title == null) {
            // Fetch courses in which a specific student is enrolled by username
            Optional<Student> OptionalStudent = studentDao.getStudentByUsername(username);
            if (OptionalStudent.isPresent()) {
                Query query = session.createQuery(
                        "SELECT e.course FROM Enrollment e WHERE e.student = :student");
                query.setParameter("student", OptionalStudent.get());
                return query.list();
            }


        } else if (title != null && username != null) {
            // Fetch enrollment for a specific student and course by title and username
            Optional<Student> OptionalStudent = studentDao.getStudentByUsername(username);
            Optional<Course> OptionalCourse = courseDao.getCourseByTitle(title);
            if (OptionalStudent.isPresent() && OptionalCourse.isPresent()) {
                Query query = session.createQuery(
                        "FROM Enrollment e WHERE e.course = :course AND e.student = :student");
                query.setParameter("course", OptionalCourse.get());
                query.setParameter("student", OptionalStudent.get());
                return query.list();
            }

        } else {
            // Fetch all enrollments if both title and username are null
            Query query = session.createQuery("FROM Enrollment");
            return query.list();
        }
        return null;
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
                session.close();
            }
        }

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
                session.close();
            }
        }

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
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception according to your application's needs
        } finally {
            session.clear();
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
            session.close();
        }
    }

}
