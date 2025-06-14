package com.example.service;

import com.example.dto.request.CategoryRequest;
import com.example.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAll();
    Page<CategoryResponse> paginate(Pageable pageable);
    CategoryResponse findById(Integer id);
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Integer id, CategoryRequest request);
    void delete(Integer id);
}
