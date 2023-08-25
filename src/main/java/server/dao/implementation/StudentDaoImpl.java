package server.dao.implementation;
import org.hibernate.*;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import server.dao.StudentDao;
import server.domain.Student;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentDaoImpl implements StudentDao {

    private final SessionFactory sessionFactory;

    public StudentDaoImpl( SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

    }

    @Override
    public List<Student> getStudents(){

        Session session = null;

        try {
            session = sessionFactory.openSession();
            Query query = session.createQuery("from Student");
            @SuppressWarnings("unchecked")
            List<Student> students = query.list();
            return students;
        } catch (Exception e) {
            if (session != null) {
                session.close();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void addStudent(Student student) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
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
    public void deleteByUsername(String username) {
        Optional<Student> studentToDelete = findByUsername(username);
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            studentToDelete.ifPresent(session::delete);
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
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();


    }

    @Override
    public Optional<Student> getStudentByUsername(String username) {
        return findByUsername(username);
    }


    public Optional<Student> findByUsername(String username) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Student s WHERE s.username = :username";
            Student student = (Student) session.createQuery(hql)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.ofNullable(student);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
            return Optional.empty();
        } finally {
            session.close();
        }
    }




}
