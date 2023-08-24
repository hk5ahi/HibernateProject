package server.dao.implementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import server.dao.CourseDao;
import server.domain.Course;


import java.util.List;
@Repository
public class CourseDaoImpl implements CourseDao {

    private final SessionFactory sessionFactory;


    public CourseDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Course> getAllCourses(){

        try (Session session = sessionFactory.openSession()) {

            Query<Course> query = session.createQuery("from Course ", Course.class);
            return query.getResultList();
        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addCourse(Course course) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(course);
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
