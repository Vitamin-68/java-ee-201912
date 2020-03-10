package ua.ithillel.dnepr.roman.gizatulin.example;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApacheCommon2PoolTest {
    private static final int MAX_POOL_SIZE = 5;
    private static final PooledObjectFactory<User> POOLED_USER_FACTORY = new PooledUserFactory();
    private static final GenericObjectPool<User> GENERIC_USER_POOL = new GenericObjectPool<>(POOLED_USER_FACTORY);
    private static final GenericKeyedObjectPool<String, User> USER_GENERIC_KEYED_OBJECT_POOL = new GenericKeyedObjectPool(new KeyedPooledUserFactory());

    @BeforeAll
    static void setup() {
        GENERIC_USER_POOL.setMaxTotal(MAX_POOL_SIZE);
    }

    @Test
    void test() throws Exception {
        User user = null;
        for (int i = 0; i < MAX_POOL_SIZE + 1; i++) {
            try {
                User left = USER_GENERIC_KEYED_OBJECT_POOL.borrowObject("left");
                user = GENERIC_USER_POOL.borrowObject(100);
                assertNotNull(user);
                assertNotNull(user.id);
                assertNull(user.firstName);
                assertNull(user.lastName);
                assertEquals(-1, user.age);

                user.firstName = "firstName_" + i;
                user.firstName = "lastName_" + i;
                user.age = i + 100;
            } catch (NoSuchElementException e) {
                assertNotNull(user);
                GENERIC_USER_POOL.returnObject(user);
                assertNull(user.id);
                assertNull(user.firstName);
                assertNull(user.lastName);
                assertEquals(-1, user.age);
                final User newUser = GENERIC_USER_POOL.borrowObject(100);
                assertSame(user, newUser);
            }
        }
    }
}

class User {
    String id;
    String firstName;
    String lastName;
    int age = -1;
}

class PooledUserFactory implements PooledObjectFactory<User> {
    @Override
    public PooledObject<User> makeObject() {
        return new DefaultPooledObject<>(new User());
    }

    @Override
    public void destroyObject(PooledObject<User> p) {
        final User user = p.getObject();
        deactivateObject(user);
    }

    @Override
    public boolean validateObject(PooledObject<User> p) {
        final User user = p.getObject();
        return Objects.nonNull(user.id);
    }

    @Override
    public void activateObject(PooledObject<User> p) {
        final User user = p.getObject();
        user.id = UUID.randomUUID().toString();
    }

    @Override
    public void passivateObject(PooledObject<User> p) {
        final User user = p.getObject();
        deactivateObject(user);
    }

    private void deactivateObject(User user) {
        user.id = null;
        user.firstName = null;
        user.lastName = null;
        user.age = -1;
    }
}

class KeyedPooledUserFactory implements KeyedPooledObjectFactory<String, User> {
    @Override
    public PooledObject<User> makeObject(String key) {
        return new DefaultPooledObject<>(new User());
    }

    @Override
    public void destroyObject(String key, PooledObject<User> p) {
        final User user = p.getObject();
        deactivateObject(user);
    }

    @Override
    public boolean validateObject(String key, PooledObject<User> p) {
        final User user = p.getObject();
        return Objects.nonNull(user.id);
    }

    @Override
    public void activateObject(String key, PooledObject<User> p) {
        final User user = p.getObject();
        user.id = UUID.randomUUID().toString();
    }

    @Override
    public void passivateObject(String key, PooledObject<User> p) {
        final User user = p.getObject();
        deactivateObject(user);
    }

    private void deactivateObject(User user) {
        user.id = null;
        user.firstName = null;
        user.lastName = null;
        user.age = -1;
    }
}
