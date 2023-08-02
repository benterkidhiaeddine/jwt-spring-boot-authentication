package com.example.springboot.journalcrud;


import com.example.springboot.journal.Journal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JournalController {


    private final JournalService journalService;
    @PostMapping("/journals")
    public ResponseEntity<MessageResponse> postJournal(@Valid  @RequestBody JournalRequest request){
        return ResponseEntity.ok(journalService.addJournal(request));
    }

    @GetMapping("/journals/{JournalId}")
    public ResponseEntity<Journal> getJournal(@PathVariable("JournalId") Integer id){

        var journal = journalService.getJournalById(id);
        return ResponseEntity.ok(journal);
    }

    @PutMapping("/journals/{journalId}")
    public ResponseEntity<MessageResponse> modifyJournal (@PathVariable("journalId") Integer id , @Valid @RequestBody JournalRequest request){
        return ResponseEntity.ok(journalService.modifyJournal(request,id));
    }



}
