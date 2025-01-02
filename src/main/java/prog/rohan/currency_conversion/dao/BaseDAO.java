package prog.rohan.currency_conversion.dao;

import java.util.Optional;
import java.util.List;

public interface BaseDAO<T, ID>{

    Optional<T> findByID(ID id);

    List<T> findAll();

    T save(T entity);

    Optional<T> update(T entity);

}
