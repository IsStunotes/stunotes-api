package com.example.service;

import com.example.dto.request.CommentRequest;
import com.example.dto.request.DocumentRequest;
import com.example.dto.response.CommentResponse;
import com.example.dto.response.DocumentResponse;
import com.example.mapper.DocumentMapper;
import com.example.model.Document;
import com.example.model.Repository;
import com.example.model.User;
import com.example.repository.DocumentRepository;
import com.example.repository.RepositoryRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceUnitTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RepositoryRepository repositoryRepository;

    @Mock
    private DocumentMapper documentMapper;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("CP00- Registrar documento - usuario y repositorio existen - devuelve respuesta")
    void registrarDocumento_usuarioYRepositorioExisten_devuelveRespuesta() {
        DocumentRequest request = new DocumentRequest(
                "Titulo Test", "Descripcion Test", 1L, 2, 1L,
                LocalDateTime.now(), List.of()
        );
        User user = new User();
        Repository repository = new Repository();
        Document document = new Document();
        Document savedDocument = new Document();
        DocumentResponse response = new DocumentResponse(1L, "Titulo Test", "Descripcion Test", 1L, 2, 1L, LocalDateTime.now(), List.of());

        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(repositoryRepository.findById(1L)).thenReturn(Optional.of(repository));
        when(documentRepository.existsByTitleAndRepositorio_Id("Titulo Test", 1L)).thenReturn(false);
        when(documentMapper.fromRequest(request, user, repository)).thenReturn(document);
        when(documentRepository.save(document)).thenReturn(savedDocument);
        when(documentMapper.toResponse(savedDocument)).thenReturn(response);

        DocumentResponse result = documentService.saveDocument(request);

        assertNotNull(result);
        assertEquals("Titulo Test", result.title());
        verify(documentRepository).save(document);
    }
    @Test
    @DisplayName("CP57- Registrar documento -usuario no existe - lanza excepción")
    void registrarDocumento_usuarioNoExiste_lanzaExcepcion() {
        DocumentRequest request = new DocumentRequest(
                "Titulo Test", "Descripcion Test", 1L, 2, 1L,
                LocalDateTime.now(), List.of()
        );

        // Simular que el usuario no existe
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.saveDocument(request));

        assertEquals("Usuario no encontrado", exception.getMessage());
    }


    @Test
    @DisplayName("CP56- Registrar documento - título duplicado - lanza excepción")
    void registrarDocumento_tituloDuplicado_lanzaExcepcion() {
        DocumentRequest request = new DocumentRequest(
                "Titulo Duplicado", "Descripcion Test", 1L, 2, 1L,
                LocalDateTime.now(), List.of()
        );

        when(userRepository.findById(2)).thenReturn(Optional.of(new User()));
        when(repositoryRepository.findById(1L)).thenReturn(Optional.of(new Repository()));

        when(documentRepository.existsByTitleAndRepositorio_Id("Titulo Duplicado", 1L)).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.saveDocument(request));

        assertEquals("Ya existe un documento con el mismo título en este repositorio", exception.getMessage());
    }


    @Test
    @DisplayName("CP62- Obtener todos los documentos - devuelve lista de respuestas")
    void obtenerTodosDocumentos_devuelveListaDeRespuestas() {
        Document document = new Document();
        DocumentResponse response = new DocumentResponse(1L, "Titulo", "Descripcion", 1L, 2, 1L, LocalDateTime.now(), List.of());

        when(documentRepository.findAll()).thenReturn(List.of(document));
        when(documentMapper.toResponse(document)).thenReturn(response);

        List<DocumentResponse> result = documentService.getAllDocuments();

        assertEquals(1, result.size());
        assertEquals("Titulo", result.getFirst().title());
    }

    @Test
    @DisplayName("CP64- Obtener documento por ID - existente - devuelve respuesta")
    void obtenerDocumentoPorId_existente_devuelveRespuesta() {
        Document document = new Document();
        DocumentResponse response = new DocumentResponse(1L, "Titulo", "Descripcion", 1L, 2, 1L, LocalDateTime.now(), List.of());

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentMapper.toResponse(document)).thenReturn(response);

        DocumentResponse result = documentService.getDocumentById(1L);

        assertNotNull(result);
        assertEquals("Titulo", result.title());
    }

    @Test
    @DisplayName("CP65- Obtener documento por ID - no existente - devuelve null")
    void obtenerDocumentoPorId_noExistente_devuelveNull() {
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        DocumentResponse result = documentService.getDocumentById(1L);

        assertNull(result);
    }

    @Test
    @DisplayName("CP58- Eliminar documento - existente - no lanza excepción")
    void eliminarDocumento_existente_noLanzaExcepcion() {
        doNothing().when(documentRepository).deleteById(1L);

        assertDoesNotThrow(() -> documentService.deleteDocument(1L));
        verify(documentRepository).deleteById(1L);
    }
    @Test
    @DisplayName("CP59- Eliminar documento - no existente - lanza excepción")
    void eliminarDocumento_noExistente_lanzaExcepcion() {
        doThrow(new RuntimeException("Documento no encontrado")).when(documentRepository).deleteById(1L);

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.deleteDocument(1L));

        assertEquals("Documento no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("CP45- Obtener comentarios por documento - existente - devuelve lista")
    void obtenerComentariosPorDocumento_existente_devuelveLista() {
        Document document = new Document();
        DocumentResponse response = new DocumentResponse(1L, "Titulo", "Descripcion", 1L, 2, 1L, LocalDateTime.now(), List.of(new CommentResponse(1L, "Comentario", "12-3-2023", 1L, 2)));

        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentMapper.toResponse(document)).thenReturn(response);

        List<CommentResponse> result = documentService.getCommentsByDocumentId(1L);

        assertEquals(1, result.size());
        assertEquals("Comentario", result.getFirst().content());
    }

    @Test
    @DisplayName("CP40- Obtener documentos por usuario - devuelve ordenados por fecha descendente")
    void obtenerDocumentosPorUsuario_devuelveOrdenados() {
        Document document = new Document();
        DocumentResponse response = new DocumentResponse(1L, "Titulo", "Descripcion", 1L, 2, 1L, LocalDateTime.now(), List.of());

        when(documentRepository.findByUsuarioId(2)).thenReturn(List.of(document));
        when(documentMapper.toResponse(document)).thenReturn(response);

        List<DocumentResponse> result = documentService.getDocumentsByUserId(2);

        assertEquals(1, result.size());
        assertEquals("Titulo", result.getFirst().title());
    }

    @Test
    @DisplayName("CP41- Obtener documentos por usuario - no existen - devuelve lista vacía")
    void obtenerDocumentosPorUsuario_noExisten_devuelveListaVacia() {
        when(documentRepository.findByUsuarioId(2)).thenReturn(List.of());

        List<DocumentResponse> result = documentService.getDocumentsByUserId(2);

        assertTrue(result.isEmpty());
    }


    @Test
    @DisplayName("CP50- Obtener documentos por repositorio - devuelve lista de respuestas")
    void obtenerDocumentosPorRepositorio_devuelveListaDeRespuestas() {
        Document document = new Document();
        DocumentResponse response = new DocumentResponse(1L, "Titulo", "Descripcion", 1L, 2, 1L, LocalDateTime.now(), List.of());

        when(repositoryRepository.findById(1L)).thenReturn(Optional.of(new Repository()));
        when(documentRepository.findByRepositorioId(1L)).thenReturn(List.of(document));
        when(documentMapper.toResponse(document)).thenReturn(response);

        List<DocumentResponse> result = documentService.getDocumentsByRepositorioId(1L);

        assertEquals(1, result.size());
        assertEquals("Titulo", result.getFirst().title());
    }

    @Test
    @DisplayName("CP42- Obtener documentos por usuario - no existe usuario ")
    void obtenerDocumentosPorUsuario_noExisteUsuario() {
        when(documentRepository.findByUsuarioId(2)).thenReturn(List.of());

        List<DocumentResponse> result = documentService.getDocumentsByUserId(2);

        assertTrue(result.isEmpty());
    }


    @Test
    @DisplayName("CP51- Obtener documentos por repositorio - no existen - devuelve lista vacía")
    void obtenerDocumentosPorRepositorio_noExisten_devuelveListaVacia() {
        // Simular que el repositorio sí existe
        when(repositoryRepository.findById(1L)).thenReturn(Optional.of(new Repository()));
        // Simular que no hay documentos
        when(documentRepository.findByRepositorioId(1L)).thenReturn(List.of());

        List<DocumentResponse> result = documentService.getDocumentsByRepositorioId(1L);

        assertTrue(result.isEmpty());
    }
    @Test
    @DisplayName("CP46-Obtener comentarios por documento - no existen comentarios- Lista vacía")
    void obtenerComentariosPorDocumento_noExistenComentarios_listaVacia() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(new Document()));
        when(documentMapper.toResponse(any(Document.class))).thenReturn(new DocumentResponse(1L, "Titulo", "Descripcion", 1L, 2, 1L, LocalDateTime.now(), List.of()));

        List<CommentResponse> result = documentService.getCommentsByDocumentId(1L);

        assertTrue(result.isEmpty());
    }
    @Test
    @DisplayName("CP47-Obtener comentarios por documento - no existe documento - lanza exception")
    void obtenerComentariosPorDocumento_noExisteDocumento_lanzaExcepcion() {
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.getCommentsByDocumentId(1L));

        assertEquals("Documento no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("CP52- Obtener documento por repositorio - repositorio no existe  - lanza excepción")
    void obtenerDocumentoPorRepositorio_repositorioNoExiste_lanzaExcepcion() {
        when(repositoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.getDocumentsByRepositorioId(1L));

        assertEquals("Repositorio no encontrado", exception.getMessage());
    }
    @Test
    @DisplayName("CP63- Obtener todos los documentos - no existen - devuelve lista vacía")
    void obtenerTodosDocumentos_noExisten_devuelveListaVacia() {
        when(documentRepository.findAll()).thenReturn(List.of());

        List<DocumentResponse> result = documentService.getAllDocuments();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("CP43- Obtener documento por versión - existente - devuelve respuesta")
    void obtenerDocumentoPorVersion_existente_devuelveRespuesta() {
        Document document = new Document();
        DocumentResponse response = new DocumentResponse(1L, "Titulo", "Descripcion", 1L, 2, 1L, LocalDateTime.now(), List.of());

        when(documentRepository.findByIdAndVersion(1L, 1L)).thenReturn(List.of(document));
        when(documentMapper.toResponse(document)).thenReturn(response);

        DocumentResponse result = documentService.getDocumentByVersion(1L, 1L);

        assertNotNull(result);
        assertEquals("Titulo", result.title());
    }
    @Test
    @DisplayName("CP44- Obtener documento por versión - no existente - devuelve null")
    void obtenerDocumentoPorVersion_noExistente_devuelveNull() {
        when(documentRepository.findByIdAndVersion(1L, 1L)).thenReturn(List.of());

        DocumentResponse result = documentService.getDocumentByVersion(1L, 1L);

        assertNull(result);
    }

    @Test
    @DisplayName("CP60- Eliminar documento por versión - existente - elimina correctamente")
    void eliminarDocumentoPorVersion_existente_eliminaCorrectamente() {
        Document document = new Document();

        when(documentRepository.findByIdAndVersion(1L, 1L)).thenReturn(List.of(document));

        documentService.deleteDocumentByVersion(1L, 1L);

        verify(documentRepository).delete(document);
    }

    @Test
    @DisplayName("CP61- Eliminar documento por versión - no existente - lanza excepción")
    void eliminarDocumentoPorVersion_noExistente_lanzaExcepcion() {
        when(documentRepository.findByIdAndVersion(1L, 1L)).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.deleteDocumentByVersion(1L, 1L));

        assertEquals("Documento no encontrado con la versión especificada", exception.getMessage());
    }
}
