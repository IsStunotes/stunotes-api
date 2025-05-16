package com.example.service;


import com.example.model.Reminder;
import com.example.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReminderServiceImpl implements ReminderService{

    private final ReminderRepository reminderRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Reminder> getAll(){
        return reminderRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Reminder findById(Integer id){
        return reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found with id: " + id));
    }

    @Transactional
    @Override
    public Reminder create(Reminder reminder){
        //reminder.setCreatedAt(LocalDateTime.now())
        return reminderRepository.save(reminder);
    }

    @Transactional
    @Override
    public Reminder update(Integer id, Reminder reminder){
        Reminder reminderDb = findById(id);
        reminderDb.setDateTime(reminder.getDateTime());//datetime
        reminderDb.setActivity(reminder.getActivity());
        return reminderRepository.save(reminderDb);
    }

    @Transactional
    @Override
    public void delete(Integer id){
        Reminder reminder = findById(id);
        reminderRepository.delete(reminder);
    }
}
