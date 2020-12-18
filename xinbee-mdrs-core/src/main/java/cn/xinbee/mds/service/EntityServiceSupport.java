package cn.xinbee.mdrs.service;

import cn.xinbee.mdrs.data.AbstractEntity;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract Class for generic CRUD operations on a service for a specific type.
 *
 * @author Xu Cheng
 */
@Transactional
public abstract class EntityServiceSupport<T extends AbstractEntity<ID>, ID extends Serializable, TRepository extends
    CrudRepository<T, ID>> implements EntityService<T, ID> {

    private final TRepository repository;

    private Class<T> entityClass;

    /**
     * Gets the entity class.
     *
     * @return the entity class
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * Create a new EntityServiceSupport instance.
     *
     * @param repository
     */
    @SuppressWarnings("unchecked")
    protected EntityServiceSupport(TRepository repository) {
        this.repository = repository;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments
                ()[0];
    }

    /**
     * Create a new EntityServiceSupport instance.
     *
     * @param repository
     * @param entityClass
     */
    protected EntityServiceSupport(TRepository repository, Class<T> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    /**
     * Gets the specific repository.
     *
     * @return the specific repository
     */
    protected TRepository getRepository() {
        return this.repository;
    }

    public <S extends T> S create(S entity) {
        if (entity.getId() != null && repository.findById(entity.getId()).isPresent()) {
            throw new DataIntegrityViolationException(String.format("%s entity with id %s exists!",
                entityClass, entity.getId()));
        }
        return repository.save(entity);
    }

    public <S extends T> S update(S entity) {
        if (!repository.findById(entity.getId()).isPresent()) {
            throw new EmptyResultDataAccessException(
                String.format("No %s entity with id %s exists!",
                    entityClass, entity.getId()), 1);
        }
        return repository.save(entity);
    }

    public <S extends T> S save(S entity) {
        return repository.save(entity);
    }

    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Transactional(readOnly = true)
    public T findById(ID id) {
        return findById(id, false);
    }

    private T findById(ID id, boolean required) {
        Optional<T> entity = repository.findById(id);
        if (required && !entity.isPresent()) {
            throw new EmptyResultDataAccessException(
                String.format("No %s entity with id %s exists!",
                    entityClass, id), 1);
        }
        return entity.orElse(null);
    }

    @Transactional(readOnly = true)
    public T getById(ID id) {
        return findById(id, true);
    }

    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Iterable<T> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Iterable<T> getAll(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    public T delete(ID id) {
        T entity = getById(id);
        delete(entity);
        return entity;
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void delete(Iterable<? extends T> entities) {
        repository.deleteAll(entities);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
