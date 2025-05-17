package com.example.service;

import com.example.model.Collection;
import java.util.List;

public interface CollectionService {
    List<Collection> getAllByUserId(Integer userId);
    Collection findById(Integer id);
    Collection create(Collection collection);
    Collection update(Integer id, Collection updateCollection);
    void delete(Integer id);

}
