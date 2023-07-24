package com.example.springboot.journalcrud;


import com.example.springboot.journal.Journal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JournalController {


    private final JournalService journalService;
    @PostMapping("/journals")
    public ResponseEntity<MessageResponse> postJournal(@RequestBody JournalRequest request){
        return ResponseEntity.ok(journalService.addJournal(request));
    }

    @GetMapping("/journals/{JournalId}")
    public ResponseEntity<Journal> getJournal(@PathVariable("JournalId") Integer id){

        var jounral = journalService.getJournalById(id);
        return ResponseEntity.ok(jounral);
    }





}
