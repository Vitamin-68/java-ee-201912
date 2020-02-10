package ua.hillel.jpaRepo;

import ua.hillel.entity.City;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class CityJpa {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistence-config");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

    }
}
