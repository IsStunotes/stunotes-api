package com.example.mapper;

import com.example.dto.request.NoteRequest;
import com.example.dto.response.NoteResponse;
import com.example.model.Collection;
import com.example.model.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    public Note toEntity(NoteRequest request) {
        Note note = new Note();
        note.setTitle(request.title());
        note.setContent(request.content());

        if (request.collectionId() != null) {
            Collection collection = new Collection();
            collection.setId(request.collectionId());
            note.setCollection(collection);
        }

        return note;
    }

    public NoteResponse toResponse(Note note){
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getCollection() != null ? note.getCollection().getName() : null
        );
    }

    public void updateEntityFromRequest(Note note, NoteRequest request) {
        if (request.title() != null) { note.setTitle(request.title()); }
        if (request.content() != null) { note.setContent(request.content()); }
        if (request.collectionId() != null) {
            Collection collection = new Collection();
            collection.setId(request.collectionId());
            note.setCollection(collection);
        }
    }
}
