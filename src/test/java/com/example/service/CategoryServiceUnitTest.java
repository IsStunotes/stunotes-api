package com.example.service;

import com.example.dto.request.CategoryRequest;
import com.example.dto.response.CategoryResponse;
import com.example.mapper.CategoryMapper;
import com.example.model.Category;
import com.example.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1);
        category.setName("Estudios");
        category.setCreatedAt(LocalDateTime.now());

        categoryRequest = new CategoryRequest("Estudios");
        categoryResponse = new CategoryResponse(1, "Estudios", LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("CP23 - Crear categoría con datos válidos")
    void createCategory_validData_returnsCreated() {
        when(categoryMapper.toEntity(categoryRequest)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.create(categoryRequest);

        assertNotNull(result);
        assertEquals("Estudios", result.name());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("CP24 - Listar todas las categorías")
    void getAllCategories_withData_returnsList() {
        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Trabajo");
        CategoryResponse response2 = new CategoryResponse(2, "Trabajo", LocalDateTime.now(), null);

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category, category2));
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);
        when(categoryMapper.toResponse(category2)).thenReturn(response2);

        List<CategoryResponse> result = categoryService.getAll();

        assertEquals(2, result.size());
        assertEquals("Estudios", result.get(0).name());
        assertEquals("Trabajo", result.get(1).name());
    }

    @Test
    @DisplayName("CP25 - Listar categorías sin datos")
    void getAllCategories_empty_returnsEmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CategoryResponse> result = categoryService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("CP26 - Listar categorías con paginación")
    void paginateCategories_withData_returnsPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Category> page = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        Page<CategoryResponse> result = categoryService.paginate(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Estudios", result.getContent().get(0).name());
    }

    @Test
    @DisplayName("CP27 - Obtener categoría por ID válido")
    void findCategoryById_found_returnsCategory() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.findById(1);

        assertEquals("Estudios", result.name());
    }

    @Test
    @DisplayName("CP28 - Obtener categoría inexistente")
    void findCategoryById_notFound_throwsException() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.findById(99));
        assertTrue(exception.getMessage().contains("Categoría no encontrada con ID: 99"));
    }

    @Test
    @DisplayName("CP29 - Actualizar categoría con datos válidos")
    void updateCategory_validData_returnsUpdated() {
        CategoryRequest updateRequest = new CategoryRequest("Estudios Avanzados");
        Category updatedCategory = new Category();
        updatedCategory.setId(1);
        updatedCategory.setName("Estudios Avanzados");
        updatedCategory.setUpdatedAt(LocalDateTime.now());
        CategoryResponse updatedResponse = new CategoryResponse(1, "Estudios Avanzados", LocalDateTime.now(), LocalDateTime.now());

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toResponse(updatedCategory)).thenReturn(updatedResponse);

        CategoryResponse result = categoryService.update(1, updateRequest);

        assertEquals("Estudios Avanzados", result.name());
        assertNotNull(result.updatedAt());
        verify(categoryMapper).updateEntityFromRequest(category, updateRequest);
    }

    @Test
    @DisplayName("CP30 - Actualizar categoría inexistente")
    void updateCategory_notFound_throwsException() {
        CategoryRequest updateRequest = new CategoryRequest("Nueva categoría");
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.update(1, updateRequest));
        assertTrue(exception.getMessage().contains("Categoría no encontrada con ID: 1"));
    }

    @Test
    @DisplayName("CP31 - Eliminar categoría existente")
    void deleteCategory_found_executesDelete() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        categoryService.delete(1);

        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("CP32 - Eliminar categoría inexistente")
    void deleteCategory_notFound_throwsException() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.delete(1));
        assertTrue(exception.getMessage().contains("Categoría no encontrada con ID: 1"));
    }

    @Test
    @DisplayName("CP33 - Verificar que se establece createdAt al crear")
    void createCategory_setsCreatedAt() {
        Category categoryWithoutDate = new Category();
        categoryWithoutDate.setName("Nueva Categoría");

        when(categoryMapper.toEntity(categoryRequest)).thenReturn(categoryWithoutDate);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category saved = invocation.getArgument(0);
            assertNotNull(saved.getCreatedAt());
            return saved;
        });
        when(categoryMapper.toResponse(any(Category.class))).thenReturn(categoryResponse);

        categoryService.create(categoryRequest);

        verify(categoryRepository).save(argThat(cat -> cat.getCreatedAt() != null));
    }

    @Test
    @DisplayName("CP34 - Verificar que se establece updatedAt al actualizar")
    void updateCategory_setsUpdatedAt() {
        CategoryRequest updateRequest = new CategoryRequest("Categoría Actualizada");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category saved = invocation.getArgument(0);
            assertNotNull(saved.getUpdatedAt());
            return saved;
        });
        when(categoryMapper.toResponse(any(Category.class))).thenReturn(categoryResponse);

        categoryService.update(1, updateRequest);

        verify(categoryRepository).save(argThat(cat -> cat.getUpdatedAt() != null));
    }
}