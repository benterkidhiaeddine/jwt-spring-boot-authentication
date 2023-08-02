package com.example.springboot.journalcrud;


import com.example.springboot.journal.Journal;
import com.example.springboot.journal.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//The required args constructor is for final fields so they can get injected automaticaly by the spring application
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;

    public MessageResponse addJournal( JournalRequest request){
        var journal = Journal.builder()
                .description(request.getDescription())
                .build();
        journalRepository.save(journal);

        return MessageResponse.builder()
                .message("Journal added successfully")
                .build();
    }


    public Journal getJournalById(Integer id){

        return journalRepository.findById(id).orElseThrow();

    }

    public MessageResponse modifyJournal(JournalRequest request,Integer id ){
        var journal = getJournalById(id);
        journal.setDescription(request.getDescription());
        journalRepository.save(journal);

        return MessageResponse.builder()
                .message("Journal modified successfully")
                .build();
    }

}
