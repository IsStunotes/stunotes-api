package com.example.service;

import com.example.dto.request.NoteRequest;
import com.example.dto.response.CollectionResponse;
import com.example.dto.response.NoteResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.NoteMapper;
import com.example.model.Collection;
import com.example.model.Note;
import com.example.repository.CollectionRepository;
import com.example.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final CollectionRepository collectionRepository;
    private final NoteMapper noteMapper;

    @Transactional(readOnly = true)
    public Page<NoteResponse> paginate(Integer user_id, Pageable pageable) {
        Page<Note> notes = noteRepository.findByCollection_User_Id(user_id, pageable);
        return notes.map(noteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse findById(Integer id) {
        Note note = noteRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("Nota no encontrada"));
        return noteMapper.toResponse(note);
    }

    @Override
    @Transactional
    public NoteResponse create(NoteRequest request) {
        Note note = noteMapper.toEntity(request);

        if (request.collectionId() != null) {
           Collection collection = collectionRepository.findById(request.collectionId())
                   .orElseThrow( ()-> new ResourceNotFoundException("Colección no encontrada") );
           note.setCollection(collection);
        }

        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(note.getCreatedAt());
        Note savedNote = noteRepository.save(note);
        return noteMapper.toResponse(savedNote);
    }

    @Override
    @Transactional
    public NoteResponse update(Integer id, NoteRequest request) {

        Note note = noteRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("Nota no encontrada"));

        noteMapper.updateEntityFromRequest(note, request);

        if (request.collectionId() != null) {
            Collection collection = collectionRepository.findById(request.collectionId())
                    .orElseThrow(()-> new ResourceNotFoundException("Coleccion no encontrada"));
            note.setCollection(collection);
        }
        note.setUpdatedAt(LocalDateTime.now());
        Note updatedNote = noteRepository.save(note);

        return noteMapper.toResponse(updatedNote);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Note note = noteRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("Nota no encontrada"));
        noteRepository.delete(note);
    }


    @Transactional(readOnly = true)
    public Page<NoteResponse> filterByCollection(Integer collectionId, Pageable pageable) {
        Page<Note> notes = noteRepository.findByCollection_Id(collectionId, pageable);
        return notes.map(noteMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NoteResponse> filterByTitleOrName(Integer user_id, String keyword, Pageable pageable) {
        Page<Note> notes = noteRepository.findByUserIdAndTitleOrCollectionName(user_id, keyword, pageable);
        return notes.map(noteMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> filterByCollection(Integer collectionId){
        collectionRepository.findById(collectionId).orElseThrow( ()-> new ResourceNotFoundException("Coleccion no encontrada"));
        return noteRepository.findByCollection_Id(collectionId)
                .stream()
                .map(noteMapper::toResponse)
                .sorted(Comparator.comparing(NoteResponse::updatedAt).reversed())
                .toList();
    }

}
