package com.example.mapper;

import com.example.dto.request.CollectionRequest;
import com.example.dto.response.CollectionResponse;
import com.example.model.Collection;
import com.example.model.User;
import org.springframework.stereotype.Component;

@Component
public class CollectionMapper {
    public Collection toEntity(CollectionRequest request) {
        Collection collection = new Collection();
        collection.setName(request.name());
        if (request.userId() != null) {
            User user = new User();
            user.setId(request.userId());
            collection.setUser(user);
        }

        return collection;
    }

    public CollectionResponse toResponse(Collection collection) {
        return new CollectionResponse(collection.getId(),
                collection.getName(),
                collection.getCreatedAt(),
                collection.getUpdatedAt(),
                collection.getUser().getId());
    }
    public void updateEntityFromRequest(Collection collection, CollectionRequest request) {
        collection.setName(request.name());
    }
}
