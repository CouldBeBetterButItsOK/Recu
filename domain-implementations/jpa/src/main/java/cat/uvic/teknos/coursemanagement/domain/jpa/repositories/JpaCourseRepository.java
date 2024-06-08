package cat.uvic.teknos.coursemanagement.domain.jpa.repositories;

import cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaCourse;
import cat.uvic.teknos.coursemanagement.models.Course;
import cat.uvic.teknos.coursemanagement.repositories.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.HashSet;
import java.util.Set;

public class JpaCourseRepository implements CourseRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaCourseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Course course) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            if (course.getId() == 0) {
                entityManager.persist(course);
            } else {
                entityManager.merge(course);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Aquí podrías usar un framework de logging
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Course course) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            Course mergedCourse = entityManager.merge(course);
            entityManager.remove(mergedCourse);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Aquí podrías usar un framework de logging
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Course get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(JpaCourse.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Set<Course> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return new HashSet<>(entityManager.createQuery("SELECT c FROM JpaCourse c", JpaCourse.class).getResultList());
        } finally {
            entityManager.close();
        }
    }
}
