package ua.ithillel.dnepr.roman.gizatulin.jpa.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        final EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("persistence-unit");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();

        for (int i = 0; i < 10; i++) {
            final User user = new User();
            user.setFName("test_first_name_" + i);
            user.setLName("test_last_name_" + i);
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
        }

        transaction.begin();
        final List<User> result = entityManager.createQuery("from User", User.class).getResultList();
        for (User user : result) {
            String message = String.format("id: %s; fName: %s; lName: %s", user.getId(), user.getFName(), user.getLName());
            System.out.println(message);
        }
        transaction.commit();


        String queryString = "SELECT a FROM User a " +
                "WHERE LOWER(a.lName) = :lName";
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        query.setParameter("lName", "test_first_name_5");
        for (User user : query.getResultList()) {
            String message = String.format("id: %s; fName: %s; lName: %s", user.getId(), user.getFName(), user.getLName());
            System.out.println(message);
        }

        for (int i = 0; i < 10; i++) {
            final Country country = new Country();
            country.setName("test_country_" + i);
            transaction.begin();
            entityManager.persist(country);
            transaction.commit();
        }

        entityManager.close();
        entityManagerFactory.close();
    }
}
