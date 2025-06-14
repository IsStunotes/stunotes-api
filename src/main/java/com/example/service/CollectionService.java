package com.example.service;

import com.example.dto.request.CollectionRequest;
import com.example.dto.response.CollectionResponse;
import com.example.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CollectionService {

    CollectionResponse create(CollectionRequest request);

    List<CollectionResponse> findAll();

    Page<CollectionResponse> findAll(Pageable pageable);
    CollectionResponse update(Integer id, CollectionRequest request);
    void delete(Integer id);

    CollectionResponse findById(Integer id);

    /*Page<Collection> findAll(Integer userId, Pageable pageable);
    List<Collection> findAllByUser(Integer userId);
    Collection findById(Integer id);*/

}
