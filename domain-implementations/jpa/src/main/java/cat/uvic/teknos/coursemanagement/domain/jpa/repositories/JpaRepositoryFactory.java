package cat.uvic.teknos.coursemanagement.domain.jpa.repositories;

import cat.uvic.teknos.coursemanagement.exceptions.RepositoryException;
import cat.uvic.teknos.coursemanagement.repositories.CourseRepository;
import cat.uvic.teknos.coursemanagement.repositories.GenreRepository;
import cat.uvic.teknos.coursemanagement.repositories.RepositoryFactory;
import cat.uvic.teknos.coursemanagement.repositories.StudentRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.util.Properties;

public class JpaRepositoryFactory implements RepositoryFactory {
    private final EntityManagerFactory entityManagerFactory;

    public JpaRepositoryFactory() {
        try {
            var properties = new Properties();
            properties.load(JpaRepositoryFactory.class.getResourceAsStream("/jpa.properties"));

            entityManagerFactory = Persistence.createEntityManagerFactory("course-management", properties);
        } catch (IOException e) {
            throw new RepositoryException();
        }
    }

    @Override
    public GenreRepository getGenreRepository() {
        return new JpaGenreRepository(entityManagerFactory);
    }

    @Override
    public CourseRepository getCourseRepository() {
        return new JpaCourseRepository(entityManagerFactory);
    }

    @Override
    public StudentRepository getStudentRepository() {
        return null;
    }
}
