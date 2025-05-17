package com.example.service;

import com.example.repository.CollectionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.model.Collection;
import org.springframework.transaction.annotation.Transactional;
import java.lang.Override;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Collection> getAllByUserId(Integer userId) {
        return collectionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Collection findById(Integer id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));
    }

    public Collection create(Collection collection) {
        collection.setCreatedAt(LocalDateTime.now());
        return collectionRepository.save(collection);
    }

    @Transactional
    @Override
    public Collection update(Integer id, Collection updateCollection) {
        Collection collectionFromDb = findById(id);
        collectionFromDb.setName(updateCollection.getName());
        return collectionRepository.save(collectionFromDb);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Collection collection = findById(id);
        collectionRepository.delete(collection);
    }

}
