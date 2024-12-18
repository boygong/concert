package com.gong.concert.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditConcertDTO {
    private String concertId;
    private String pass; //yes,no
}
