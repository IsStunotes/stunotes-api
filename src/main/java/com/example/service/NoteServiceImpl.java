package com.example.service;

import com.example.dto.request.NoteRequest;
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

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final CollectionRepository collectionRepository;
    private final NoteMapper noteMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NoteResponse> paginate(Pageable pageable) {
        Page<Note> notes = noteRepository.findAll(pageable);
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
        Note savedNote = noteRepository.save(note);
        return noteMapper.toResponse(savedNote);
    }

    @Override
    @Transactional
    public NoteResponse update(Integer id, NoteRequest request) {

        Note noteFromDb = noteRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("Nota no encontrada"));

        noteMapper.updateEntityFromRequest(noteFromDb, request);

        if (request.collectionId() != null) {
            Collection collection = collectionRepository.findById(request.collectionId())
                    .orElseThrow(()-> new ResourceNotFoundException("Coleccion no encontrada"));
            noteFromDb.setCollection(collection);
        }

        Note updatedNote = noteRepository.save(noteFromDb);
        return noteMapper.toResponse(updatedNote);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Note note = noteRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("Nota no encontrada"));
        noteRepository.delete(note);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteResponse> filterByCollection(String collectionName, Pageable pageable) {
        Page<Note> notes = noteRepository.findByCollection_Name(collectionName, pageable);
        return notes.map(noteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteResponse> sortByUpdatedAt(Pageable pageable) {
        Page<Note> notes = noteRepository.findAllByOrderByUpdatedAtDesc(pageable);
        return notes.map(noteMapper::toResponse);
    }

}
