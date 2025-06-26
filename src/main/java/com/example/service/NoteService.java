package com.example.service;

import com.example.dto.request.NoteRequest;
import com.example.dto.response.NoteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    Page<NoteResponse> paginate(Pageable pageable);
    NoteResponse findById(Integer id);
    NoteResponse create(NoteRequest request);
    NoteResponse update(Integer id, NoteRequest request);
    void delete(Integer id);

    Page<NoteResponse> filterByCollection(String collectionName, Pageable pageable);
    Page<NoteResponse> sortByUpdatedAt(Pageable pageable);
}
