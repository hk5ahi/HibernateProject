package server.dao.implementation;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import server.dao.StudentDao;

import server.domain.Student;

import java.util.List;

@Repository
public class StudentDaoImpl implements StudentDao {

    private final SessionFactory sessionFactory;


    public StudentDaoImpl( SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

    }

    @Override
    public List<Student> getStudents(){

        try (Session session = sessionFactory.openSession()) {

            Query<Student> query = session.createQuery("from Student ", Student.class);
            return query.getResultList();
        } catch (Exception e) {

            e.printStackTrace();
            throw e;
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



}
