package dao;

import java.util.List;

/**
 * Created by willian on 19/06/2016.
 */
public interface Persistencia<T> {

    public void insert(T object);
    public void update(T object);
    public void delete(T object);
    public List<T> findAll();
}
