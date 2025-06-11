package com.example.service;

import com.example.dto.request.CollectionRequest;
import com.example.dto.response.CollectionResponse;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CollectionMapper;
import com.example.repository.CollectionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.model.Collection;
import org.springframework.transaction.annotation.Transactional;
import java.lang.Override;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionMapper collectionMapper;

    @Transactional
    public CollectionResponse create(CollectionRequest request) {
        if (collectionRepository.existsByName(request.name())) {
            // Manejo de exception
            throw new DuplicateResourceException("Ya existe una colección con ese nombre");
        }
        Collection saved = collectionRepository.save(collectionMapper.toEntity(request));
        return collectionMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CollectionResponse findById(Integer id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Collection no encontrado"));
        return collectionMapper.toResponse(collection);
    }

    @Transactional
    public List<CollectionResponse> findAll(){
        return collectionRepository.findAll()
                .stream()
                .map(collectionMapper::toResponse)
                .toList();
    }

    @Transactional
    public Page<CollectionResponse> findAll(Pageable pageable) {
        return collectionRepository.findAll(pageable)
                .map(collectionMapper::toResponse);
    }



    @Transactional
    public CollectionResponse update(Integer id, CollectionRequest request) {
        Collection collection = collectionRepository.findById(id).orElseThrow(null);

        // Se valida que el nombre de la colección no sea la misma o que ya exista otra con el mismo nombre
        if(!collection.getName().equals(request.name()) && collectionRepository.existsByName(request.name())){
            throw new DuplicateResourceException("Ya existe una colección con ese nombre");
        }

        collection.setName(request.name());
        return collectionMapper.toResponse(collectionRepository.save(collection));
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Colección no encontrada"));

        collectionRepository.delete(collection);
    }
}
