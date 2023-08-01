package com.example.springboot.journalcrud;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalRequest {
    @NotBlank(message = "Description is required")
    private String description;

}
