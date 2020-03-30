package ua.ithillel.dnepr.tymoshenko.olga.jparepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.tymoshenko.olga.jpa.jpaentity.Adress;
import ua.ithillel.dnepr.tymoshenko.olga.jpa.jpaentity.Company;
import ua.ithillel.dnepr.tymoshenko.olga.jpa.jpaentity.Person;
import ua.ithillel.dnepr.tymoshenko.olga.jpa.jparepository.jpaRepository;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class jpaRepositoryTest {
    private EntityManagerFactory entityManagerFactory;
    jpaRepository repository;
    Person person;

    @BeforeEach
    void generalSetUp() {
        entityManagerFactory =
                Persistence.createEntityManagerFactory("persistence-unit");

        person = new Person();
        Adress adress = new Adress();
        Company company = new Company();

        adress.setCity("Dnepr");
        adress.setStreet("Stroiteley");
        adress.setApart("21");

        company.setCompany("Acme");
        company.setDescription("Make up");
        company.setCity("Dnipro");

        person.setAge(22);
        person.setFirstName("Oleg");
        person.setLastName("Popkin");
        person.setAdress(adress);

        List<Company> listCompany = new ArrayList<>();
        listCompany.add(company);
        person.setWorkingPlaces(listCompany);

        repository = new jpaRepository(entityManagerFactory, Person.class);
    }

    @AfterEach
    void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    void findAll() {
        Optional<List<Person>> list = repository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void findById() {
        Integer id = 3;
        Optional<Person> actual = repository.findById(id);
        assertFalse(actual.isEmpty());
    }

    @Test
    void findByIdNotExistEntity() {
        Integer id = 100;
        Optional<Person> actual = repository.findById(id);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByField() {
        String fieldName = "firstName";
        String value = "Mary";
        Optional<List<Person>> actual = repository.findByField(fieldName, value);
        assertFalse(actual.isEmpty());
    }

    @Test
    void findByNotExistField() {
        String fieldName = "age";
        Integer value = 100;
        Optional<List<Person>> actual = repository.findByField(fieldName, value);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByFieldOtherClass() {
        repository = new jpaRepository(entityManagerFactory, Adress.class);
        String fieldName = "city";
        String value = "Dnipro";
        Optional<List<Person>> actual = repository.findByField(fieldName, value);
        assertFalse(actual.isEmpty());
    }

    @Test
    void findByOtherField() {
        String fieldName = "age";
        Integer value = 30;
        Optional<List<Person>> actual = repository.findByField(fieldName, value);
        assertFalse(actual.isEmpty());
    }

    @Test
    void findByNotField() {
        String fieldName = "sex";
        String value = "men";
        try {
            repository.findByField(fieldName, value);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByEmptyField() {
        String field = "";
        String value = "Mary";
        try {
            repository.findByField(field, value);
            Assert.fail("Expected IOException");
        } catch (IllegalArgumentException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByNullField() {
        String field = null;
        String value = "Mary";
        try {
            repository.findByField(field, value);
            Assert.fail("Expected IOException");
        } catch (NullPointerException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void createNotExistEntity() {
        Person actual = saveEntity(person);
        assertEquals(person, actual);
        actual = (Person) repository.delete(actual.getId());
        assertEquals(person, actual);
    }

    @Test
    void createExistEntity() {
        Integer id = 4;
        Person test = entityManagerFactory.createEntityManager().find(Person.class, id);
        Person actual = saveEntity(test);
        assertEquals(test, actual);
    }


    @Test
    void createNullEntity() {
        try {
            Person actual = saveEntity(null);
            Assert.fail("Expected IOException");
        } catch (NullPointerException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void updateExistEntity() {
        Integer id = 4;
        Person newPerson = entityManagerFactory.createEntityManager().find(Person.class, id);
        newPerson.setFirstName("Petia");
        Person actual = (Person) repository.update(newPerson);
        assertEquals(newPerson, actual);
    }

    @Test
    void updateNullEntity() {
        try {
            repository.update(null);
            Assert.fail("Expected IOException");
        } catch (NullPointerException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void deleteExistEntity() {
        person.setFirstName("Vovik");
        person.setLastName("Sidorov");
        Person person1 = saveEntity(person);
        assertEquals(person, person1);
        Integer id = 0;
        Optional<List<Person>> list = repository.findAll();
        if (!list.isEmpty()) {
            for (Person p : list.get()) {
                if (p.equals(person1))
                    id = p.getId();
            }
        }
        Person person2 = entityManagerFactory.createEntityManager().find(Person.class, id);
        assertEquals(person1, person2);
        Person actual = (Person) repository.delete(id);
        assertEquals(person2, actual);
    }

    @Test
    void deleteNotExistEntity() {
        Integer id = 27;
        try {
            repository.delete(id);
            Assert.fail("Expected IOException");
        } catch (NullPointerException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    private Person saveEntity(Person entity) {
        return (Person) repository.create(entity);

    }
}