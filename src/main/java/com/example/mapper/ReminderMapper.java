package com.example.mapper;

import com.example.dto.response.ReminderResponse;
import com.example.dto.request.ReminderRequest;
import org.springframework.stereotype.Component;
import com.example.model.Reminder;
import com.example.model.Activity;
import com.example.model.User;


@Component
public class ReminderMapper {

    public Reminder toEntity(ReminderRequest request, Activity activity, User user){
        return Reminder.builder()
                .id(request.id())
                .titulo(request.titulo())
                .dateTime(request.dateTime())
                .activity(activity)
                .user(user)
                .build();
    }


    public ReminderResponse toResponse(Reminder reminder){
        return new ReminderResponse(
                reminder.getId(),
                reminder.getTitulo(),
                reminder.getActivity() != null ? reminder.getActivity().getTitle() : null,
                reminder.getDateTime(),
                reminder.getActivity() != null ? reminder.getActivity().getId() : null
        );
    }

}