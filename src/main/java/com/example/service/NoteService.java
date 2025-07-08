package com.example.service;

import com.example.dto.request.NoteRequest;
import com.example.dto.response.NoteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteService {
    Page<NoteResponse> paginate(Integer user_id, Pageable pageable);
    NoteResponse findById(Integer id);
    NoteResponse create(NoteRequest request);
    NoteResponse update(Integer id, NoteRequest request);
    void delete(Integer id);

    Page<NoteResponse> filterByTitleOrName(Integer user_id, String keyword, Pageable pageable);
    Page<NoteResponse> filterByCollection(Integer collectionId, Pageable pageable);
    List<NoteResponse> filterByCollection(Integer collectionId);

}
