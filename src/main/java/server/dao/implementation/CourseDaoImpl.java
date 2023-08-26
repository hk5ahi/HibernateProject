package server.dao.implementation;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.dao.CourseDao;
import server.domain.Course;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseDaoImpl implements CourseDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public CourseDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Course> getAllCourses(){

        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Query query = session.createQuery("from Course");
            @SuppressWarnings("unchecked")
            List<Course> courses = query.list();

            transaction.commit();
            return courses;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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
    public void addCourse(Course course) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(course);
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
    public Optional<Course> getCourseByTitle(String title) {
        return findByTitle(title);
    }

    @Override
    public void deleteByTitle(String title) {
        Optional<Course> courseToDelete = findByTitle(title);
        Transaction transaction = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Session finalSession = session;
            courseToDelete.ifPresent(finalSession::delete);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle the exception according to your application's needs
        } finally {
            if (session != null) {
                session.clear();
                session.close();

            }
        }
    }

    @Override
    public boolean existsByTitle(String title) {
        return findByTitle(title).isPresent();
    }


    private Optional<Course> findByTitle(String title) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Course s WHERE s.title = :title";
            Course course=(Course) session.createQuery(hql)
                    .setParameter("title", title)
                    .uniqueResult();
            return Optional.ofNullable(course);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
            return Optional.empty(); // Return null or handle the error case appropriately
        } finally {
            session.clear();
            session.close();
        }
    }

}
