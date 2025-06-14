package com.example.mapper;

import com.example.dto.request.CollectionRequest;
import com.example.dto.response.CollectionResponse;
import com.example.model.Collection;
import org.springframework.stereotype.Component;

@Component
public class CollectionMapper {
    public Collection toEntity(CollectionRequest request){
        Collection collection = new Collection();
        collection.setName(request.name());
        return collection;
    }

    public CollectionResponse toResponse(Collection collection) {
        return new CollectionResponse(collection.getId(), collection.getName());
    }
}
