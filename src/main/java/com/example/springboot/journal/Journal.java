package com.example.springboot.journal;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "journal")
public class Journal {
    @Id
    @GeneratedValue
    private Integer id;
    private String description;

}
