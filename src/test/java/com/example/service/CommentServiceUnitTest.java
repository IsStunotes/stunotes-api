package com.example.service;

import com.example.dto.request.CommentRequest;
import com.example.dto.response.CommentResponse;
import com.example.mapper.CommentMapper;
import com.example.model.Comment;
import com.example.model.Document;
import com.example.model.User;
import com.example.repository.CommentRepository;
import com.example.repository.DocumentRepository;
import com.example.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("CP80- Registrar comentario - documento y usuario existen - devuelve respuesta")
    void guardarComentario_documentoYUsuarioExisten_devuelveRespuesta() {
        CommentRequest request = new CommentRequest("contenido de prueba", 3L, 1);
        Document document = new Document();
        User user = new User();
        Comment comment = new Comment();
        Comment savedComment = new Comment();
        CommentResponse response = new CommentResponse(3L, "contenido de prueba", "12-3-2023", 3L,1);

        when(documentRepository.findById(3L)).thenReturn(Optional.of(document));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(commentMapper.fromRequest(request, user, document)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.toResponse(savedComment)).thenReturn(response);

        CommentResponse result = commentService.saveComment(request);

        assertNotNull(result);
        assertEquals("contenido de prueba", result.content());
        verify(commentRepository).save(comment);
    }
    //@DisplayName("CP00- Registrar comentario - documento y usuario no existen - devuelve error"
    @Test
    @DisplayName("CP81- Registrar comentario - documento y usuario no existen - lanza excepción")
    void guardarComentario_documentoYUsuarioNoExisten_lanzaExcepcion() {
        CommentRequest request = new CommentRequest("contenido de prueba", 3L, 1);

        when(documentRepository.findById(3L)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.saveComment(request));
        verify(commentRepository, never()).save(any());
    }

    @Test
    @DisplayName("CP82- Obtener todos los comentarios - devuelve lista de respuestas")
    void obtenerTodos_losComentariosDevuelveListaDeRespuestas() {
        Comment comment = new Comment();
        CommentResponse response = new CommentResponse(5L, "contenido de prueba", "10-3-2023", 2L,1);
        when(commentRepository.findAll()).thenReturn(List.of(comment));
        when(commentMapper.toResponse(comment)).thenReturn(response);

        List<CommentResponse> result = commentService.getAllComments();

        assertEquals(1, result.size());
        assertEquals("contenido de prueba", result.getFirst().content());
    }
    @Test
    @DisplayName("CP83- Obtener comentarios pero no hay- devuelve Lista Vacia")
    void obtenerTodos_losComentariosVacio_devuelveListaVacia() {
        when(commentRepository.findAll()).thenReturn(List.of());

        List<CommentResponse> result = commentService.getAllComments();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("CP84- Comentario por ID existente - devuelve respuesta")
    void obtenerPorId_existente_devuelveRespuesta() {
        Comment comment = new Comment();
        CommentResponse response = new CommentResponse(3L, "contenido de prueba", "12-3-2023", 2L,1);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toResponse(comment)).thenReturn(response);

        CommentResponse result = commentService.getCommentById(1L);

        assertNotNull(result);
        assertEquals("contenido de prueba", result.content());
    }

    @Test
    @DisplayName("CP85- Comentario por ID no existente - devuelve null")
    void obtenerPorId_noExistente_devuelveNull() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        CommentResponse result = commentService.getCommentById(1L);

        assertNull(result);
    }

    @Test
    @DisplayName("CP86- Eliminar comentario existente - no lanza excepción")
    void eliminarComentario_existente_noLanzaExcepcion() {
        doNothing().when(commentRepository).deleteById(1L);

        assertDoesNotThrow(() -> commentService.deleteComment(1L));
        verify(commentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("CP87- Comentario por documento ID existente - devuelve comentarios")
    void obtenerPorDocumentoId_existente_devuelveComentarios() {
        Comment comment = new Comment();
        CommentResponse response = new CommentResponse(3L, "contenido de prueba", "12-3-2023", 2L,1);

        when(commentRepository.findByDocumentId(1L)).thenReturn(List.of(comment));
        when(commentMapper.toResponse(comment)).thenReturn(response);

        List<CommentResponse> result = commentService.getCommentsByDocumentId(1L);

        assertEquals(1, result.size());
        assertEquals("contenido de prueba", result.getFirst().content());
    }
    @Test
    @DisplayName("CP88- Comentario por documento ID no existente - devuelve lista vacía")
    void obtenerPorDocumentoId_noExistente_devuelveListaVacia() {
        when(commentRepository.findByDocumentId(1L)).thenReturn(List.of());

        List<CommentResponse> result = commentService.getCommentsByDocumentId(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("CP89- Comentario por usuario ID existente - devuelve comentarios")
    void obtenerPorUsuarioId_existente_devuelveComentarios() {
        Comment comment = new Comment();
        CommentResponse response = new CommentResponse(3L, "comentario usuario", "12-3-2023", 2L,1);

        when(commentRepository.findByUsuarioId(2)).thenReturn(List.of(comment));
        when(commentMapper.toResponse(comment)).thenReturn(response);

        List<CommentResponse> result = commentService.getCommentsByUsuarioId(2);

        assertEquals(1, result.size());
        assertEquals("comentario usuario", result.getFirst().content());
    }
    @Test
    @DisplayName("CP90- Comentario por usuario ID no existente - devuelve lista vacía")
    void obtenerPorUsuarioId_noExistente_devuelveListaVacia() {
        when(commentRepository.findByUsuarioId(2)).thenReturn(List.of());

        List<CommentResponse> result = commentService.getCommentsByUsuarioId(2);

        assertTrue(result.isEmpty());
    }
}
