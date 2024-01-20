package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.Hibernate;
import org.hibernate.cfg.beanvalidation.HibernateTraversableResolver;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.OperadorDeLogistica;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backend.security.Hasher;

import java.util.List;

@Stateless
public class OperadorDeLogisticaBean {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Hasher hasher;

    public boolean exists(String username) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(o.username) FROM OperadorDeLogistica o WHERE o.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long)query.getSingleResult() > 0L;
    }

    public OperadorDeLogistica create(String username, String password, String name, String email) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityExistsException(
                    "Operador de logística with username '" + username + "' already exists");
        }
        OperadorDeLogistica operadorDeLogistica = null;
        try {
            operadorDeLogistica = new OperadorDeLogistica(username, hasher.hash(password), name, email);
            entityManager.persist(operadorDeLogistica);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        return operadorDeLogistica;
    }
    public List<OperadorDeLogistica> getAll() {
        return entityManager.createNamedQuery("getAllOperadoresDeLogistica", OperadorDeLogistica.class).getResultList();
    }
    public OperadorDeLogistica find(String username) {
        return entityManager.find(OperadorDeLogistica.class, username);
    }

    public void remove(String username) throws MyEntityNotFoundException {
        OperadorDeLogistica operadorDeLogistica = find(username);
        if (operadorDeLogistica == null) {
            throw new MyEntityNotFoundException(
                    "Operador de Logística with username '" + username + "' not found"
            );
        }
        entityManager.remove(operadorDeLogistica);

    }

    public void update(String username, String name, String email) throws MyEntityNotFoundException {
        OperadorDeLogistica operadorDeLogistica = entityManager.find(OperadorDeLogistica.class, username);
        if (operadorDeLogistica == null) {
            throw new MyEntityNotFoundException(
                    "Operador de Logística with username '" + username + "' not found"
            );
        }
        entityManager.lock(operadorDeLogistica, LockModeType.OPTIMISTIC);
        operadorDeLogistica.setName(name);
        operadorDeLogistica.setEmail(email);
        entityManager.merge(operadorDeLogistica);
    }

    public OperadorDeLogistica getOperadorWithEncomendas(String username) throws MyEntityNotFoundException {
        OperadorDeLogistica operadorDeLogistica = find(username);
        if (operadorDeLogistica == null) {
            throw new MyEntityNotFoundException(
                    "Operador de Logística with username '" + username + "' not found"
            );
        }
        Hibernate.initialize(operadorDeLogistica.getEncomendas());
        return operadorDeLogistica;
    }
}
