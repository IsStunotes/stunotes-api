package com.example.service;

import com.example.dto.request.RepositoryRequest;
import com.example.dto.response.DocumentResponse;
import com.example.dto.response.RepositoryResponse;
import com.example.mapper.RepositoryMapper;
import com.example.model.Repository;
import com.example.model.User;
import com.example.repository.RepositoryRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceUnitTest {

    @InjectMocks
    private RepositoryService repositoryService;

    @Mock
    private RepositoryRepository repositoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RepositoryMapper repositoryMapper;

    private final User user = new User();
    private final Repository repository = new Repository();

    private final DocumentResponse documentResponse = new DocumentResponse(
            1L, "Título", "Descripción", 1L, 1, 1L, LocalDateTime.now(), List.of()
    );

    private final RepositoryResponse repositoryResponse = new RepositoryResponse(
            1L, List.of(documentResponse)
    );

    @Test
    @DisplayName("CP53 - Obtener todos los repositorios - devuelve lista de respuestas")
    void getAllRepositories_devuelveLista() {
        when(repositoryRepository.findAll()).thenReturn(List.of(repository));
        when(repositoryMapper.toResponse(repository)).thenReturn(repositoryResponse);

        List<RepositoryResponse> result = repositoryService.getAllRepositories();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }
    @Test
    @DisplayName("CP54 - Obtener todos los repositorios - lista vacía")
    void getAllRepositories_listaVacia() {
        when(repositoryRepository.findAll()).thenReturn(List.of());

        List<RepositoryResponse> result = repositoryService.getAllRepositories();

        assertTrue(result.isEmpty());
    }


    @Test
    @DisplayName("CP66 - Guardar repositorio - usuario existe - guarda correctamente")
    void saveRepository_usuarioExiste_guardaCorrectamente() {
        RepositoryRequest request = new RepositoryRequest(1, List.of());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(repositoryMapper.fromRequest(request, user)).thenReturn(repository);
        when(repositoryRepository.save(repository)).thenReturn(repository);
        when(repositoryMapper.toResponse(repository)).thenReturn(repositoryResponse);

        RepositoryResponse result = repositoryService.saveRepository(request);

        assertEquals(1L, result.id());
        assertEquals(1, result.documents().size());
    }

    @Test
    @DisplayName("CP67 - Guardar repositorio - usuario no existe - lanza excepción")
    void saveRepository_usuarioNoExiste_lanzaExcepcion() {
        RepositoryRequest request = new RepositoryRequest(99, List.of());
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> repositoryService.saveRepository(request));

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("CP00 - Obtener repositorio por ID - existe - devuelve respuesta")
    void getRepositoryById_existe_devuelveRespuesta() {
        when(repositoryRepository.findById(1L)).thenReturn(Optional.of(repository));
        when(repositoryMapper.toResponse(repository)).thenReturn(repositoryResponse);

        RepositoryResponse result = repositoryService.getRepositoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    @DisplayName("CP00- Obtener repositorio por ID - no existe - devuelve null")
    void getRepositoryById_noExiste_devuelveNull() {
        when(repositoryRepository.findById(1L)).thenReturn(Optional.empty());

        RepositoryResponse result = repositoryService.getRepositoryById(1L);

        assertNull(result);
    }

    @Test
    @DisplayName("CP68- Eliminar repositorio por ID - elimina sin excepción")
    void deleteRepository_eliminaSinExcepcion() {
        assertDoesNotThrow(() -> repositoryService.deleteRepository(1L));
        verify(repositoryRepository).deleteById(1L);
    }
    @Test
    @DisplayName("CP69 - Eliminar repositorio por ID - no existe - lanza excepción")
    void deleteRepository_noExiste_lanzaExcepcion() {
        doThrow(new RuntimeException("Repositorio no encontrado")).when(repositoryRepository).deleteById(1L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> repositoryService.deleteRepository(1L));

        assertEquals("Repositorio no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("CP48 - Obtener repositorios por usuario ID - devuelve lista")
    void getRepositoriesByUsuarioId_devuelveLista() {
        when(repositoryRepository.findByUsuarioId(1)).thenReturn(List.of(repository));
        when(repositoryMapper.toResponse(repository)).thenReturn(repositoryResponse);

        List<RepositoryResponse> result = repositoryService.getRepositoriesByUsuarioId(1);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }
    @Test
    @DisplayName("CP49 - Obtener repositorios por usuario ID - lista vacía")
    void getRepositoriesByUsuarioId_listaVacia() {
        when(repositoryRepository.findByUsuarioId(1)).thenReturn(List.of());

        List<RepositoryResponse> result = repositoryService.getRepositoriesByUsuarioId(1);

        assertTrue(result.isEmpty());
    }


}