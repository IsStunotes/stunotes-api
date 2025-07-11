package com.example.service;

import com.example.dto.request.CollectionRequest;
import com.example.dto.response.CollectionResponse;
import com.example.dto.response.NoteResponse;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CollectionMapper;
import com.example.model.Note;
import com.example.model.User;
import com.example.repository.CollectionRepository;

import com.example.repository.NoteRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.model.Collection;
import org.springframework.transaction.annotation.Transactional;
import java.lang.Override;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionMapper collectionMapper;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    @Transactional
    public CollectionResponse create(CollectionRequest request) {

        userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));

        if (collectionRepository.existsByNameAndUser_Id(request.name(), request.userId())) {
            // Manejo de exception
            throw new DuplicateResourceException("Ya existe una colección con ese nombre para el usuario");
        }
        Collection collection = collectionMapper.toEntity(request);
        collection.setCreatedAt(LocalDateTime.now());
        collection.setUpdatedAt(collection.getCreatedAt()); // Solo al crear

        Collection saved = collectionRepository.save(collection);
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

    /*@Transactional
    public Page<CollectionResponse> findAll(Pageable pageable) {
        return collectionRepository.findAll(pageable)
                .map(collectionMapper::toResponse);
    }*/

    @Transactional
    public List<CollectionResponse> getCollectionsByUser_Id(Integer user_id) {
        userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("El usuario no existe"));

        return collectionRepository.getCollectionsByUser_Id(user_id)
                .stream()
                .map(collectionMapper::toResponse)
                .sorted(Comparator.comparing(CollectionResponse::updatedAt).reversed())
                .toList();
    }

    @Transactional
    public Page<CollectionResponse> getCollectionsByUser_Id(Pageable pageable, Integer user_id) {
        userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("El usuario no existe"));

        return collectionRepository.getCollectionsByUser_Id(pageable, user_id)
                .map(collectionMapper::toResponse);
    }


    @Override
    @Transactional
    public CollectionResponse update(Integer id, CollectionRequest request) {

        userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));

        if (collectionRepository.existsByNameAndUser_Id(request.name(), request.userId())) {
            // Manejo de exception
            throw new DuplicateResourceException("Ya existe una colección con ese nombre para el usuario");
        }

        Collection collection = collectionRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Colección no encontrada"));

        if( !collection.getUser().getId().equals(request.userId()) ) {
            throw new RuntimeException("No puede modificar esta colección, porque no le pertence");
        }

        collectionMapper.updateEntityFromRequest(collection, request);
        collection.setUpdatedAt(LocalDateTime.now());

        Collection updatedCollection = collectionRepository.save(collection);

        return collectionMapper.toResponse(updatedCollection);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Colección no encontrada"));

        List<Note> notes = noteRepository.findByCollection_Id(id);
        if (!notes.isEmpty()) {
            throw new RuntimeException("No se puede eliminar, ya que tiene notas asociada");
        }

        collectionRepository.delete(collection);
    }
}
