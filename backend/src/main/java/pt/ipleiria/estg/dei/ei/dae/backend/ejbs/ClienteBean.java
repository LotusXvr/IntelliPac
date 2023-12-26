package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backend.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.backend.security.Hasher;

import java.util.List;

@Stateless
public class ClienteBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;

    public boolean exists(String username) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(c.username) FROM Cliente c WHERE c.username = :username",
                Long.class
        );
        query.setParameter("username", username);
        return (Long) query.getSingleResult() > 0L;
    }

    public Cliente create(String username, String password, String name, String email)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityExistsException(
                    "Cliente with username '" + username + "' already exists");
        }
        Cliente cliente = null;
        try {
            cliente = new Cliente(username, hasher.hash(password), name, email);
            entityManager.persist(cliente);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        return cliente;
    }

    public List<Cliente> getAll() {
        return entityManager.createNamedQuery("getAllClientes", Cliente.class).getResultList();
    }

    public Cliente find(String username) {
        return entityManager.find(Cliente.class, username);
    }

    public void remove(String username) throws MyEntityNotFoundException {
        Cliente cliente = find(username);
        if (cliente == null) {
            throw new MyEntityNotFoundException(
                    "Cliente with username '" + username + "' not found"
            );
        }
        entityManager.remove(cliente);
    }

    public void update(String username, String password, String name, String email) throws MyEntityNotFoundException {
        Cliente cliente = entityManager.find(Cliente.class, username);
        if (cliente == null) {
            throw new MyEntityNotFoundException(
                    "Cliente with username '" + username + "' not found"
            );
        }
        entityManager.lock(cliente, LockModeType.OPTIMISTIC);
        cliente.setPassword(password);
        cliente.setName(name);
        cliente.setEmail(email);
    }
}
