package com.example.springboot.journalcrud;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/journals")
public class JournalController {


    private final JournalService journalService;
    @PostMapping("")
    public ResponseEntity<MessageResponse> postJournal(@RequestBody JournalRequest request){
        return ResponseEntity.ok(journalService.addJournal(request));
    }





}
