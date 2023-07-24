package com.example.springboot.journalcrud;


import com.example.springboot.journal.Journal;
import com.example.springboot.journal.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
//The required args constructor is for final fields so they can get injected automaticaly by the spring application
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;

    public MessageResponse addJournal(@RequestBody JournalRequest request){
        var journal = Journal.builder()
                .description(request.getDescription())
                .build();
        journalRepository.save(journal);

        return MessageResponse.builder()
                .message("Journal added successfully")
                .build();
    }

}
