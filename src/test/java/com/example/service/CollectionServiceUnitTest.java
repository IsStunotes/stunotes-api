package com.example.service;

import com.example.dto.request.CollectionRequest;
import com.example.dto.response.CollectionResponse;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CollectionMapper;
import com.example.model.Collection;
import com.example.repository.CollectionRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class CollectionServiceUnitTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private CollectionMapper collectionMapper;

    @InjectMocks
    private CollectionServiceImpl collectionService;

    private Collection collection;
    private CollectionRequest collectionRequest;
    private CollectionResponse collectionResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        collection = new Collection();
        collection.setId(1);
        collection.setName("Coleccion");
        collection.setCreatedAt(LocalDateTime.now());

        collectionRequest = new CollectionRequest("Coleccion");
        collectionResponse = new CollectionResponse(1,"Coleccion",LocalDateTime.now());

    }

    @Test
    @DisplayName("CP91 - Listar colecciones con datos")
    void listCollections_whitData_returnsList() {
        Collection c1 = new Collection();
        c1.setId(2);
        c1.setName("Coleccion de prueba 2");
        c1.setCreatedAt(LocalDateTime.now());
        Collection c2 = new Collection();
        c2.setId(3);
        c2.setName("Coleccion de prueba 3");
        c2.setCreatedAt(LocalDateTime.now());

        CollectionResponse r1 = new CollectionResponse(2, "Coleccion de prueba 2", LocalDateTime.now());
        CollectionResponse r2 = new CollectionResponse(3, "Coleccion de prueba 3", LocalDateTime.now());

        when(collectionRepository.findAll()).thenReturn(Arrays.asList(c1, c2));
        when(collectionMapper.toResponse(c1)).thenReturn(r1);
        when(collectionMapper.toResponse(c2)).thenReturn(r2);

        List<CollectionResponse> result = collectionService.findAll();
        assertEquals(2,result.size());
    }

    @Test
    @DisplayName("CP92 - Listar colecciones con paginación")
    void listCollections_whitPagination_returnsPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Collection> page = new PageImpl<>(List.of(collection));

        when(collectionRepository.findAll(pageable)).thenReturn(page);
        when(collectionMapper.toResponse(collection)).thenReturn(collectionResponse);

        Page<CollectionResponse> result = collectionService.findAll(pageable);
        assertEquals(1,result.getContent().size());
        assertEquals("Coleccion",result.getContent().get(0).name());
    }

    @Test
    @DisplayName("CP93 - obtener coleccion por ID válido")
    void getCollectionById_found_returnsCollection() {
        when(collectionRepository.findById(1)).thenReturn(Optional.of(collection));
        when(collectionMapper.toResponse(collection)).thenReturn(collectionResponse);
        CollectionResponse result = collectionService.findById(1);
        assertNotNull(result);
        assertEquals("Coleccion", result.name());
    }

    @Test
    @DisplayName("CP94 - obtener coleccion inexistente")
    void getCollectionById_notFound_throwException() {
        when(collectionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                ()-> collectionService.findById(99));
    }

    ///  --------
    @Test
    @DisplayName("CP95 - Crear colección con nombre válido")
    void createCollection_validName_returnCreated() {
        when(collectionMapper.toEntity(collectionRequest)).thenReturn(collection);
        when(collectionRepository.save(any(Collection.class))).thenReturn(collection);
        when(collectionMapper.toResponse(collection)).thenReturn(collectionResponse);

        CollectionResponse result = collectionService.create(collectionRequest);

        assertNotNull(result);
        assertEquals("Coleccion", result.name());
        verify(collectionRepository).save(any(Collection.class));
    }

    @Test
    @DisplayName("CP96 - Registar coleccion con nombre duplicado")
    void createCollection_duplicateName_throwException() {
        when(collectionRepository.existsByName("Coleccion")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                ()-> collectionService.create(collectionRequest));
    }

}
