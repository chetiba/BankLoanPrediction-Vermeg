package com.vermeg.chtiba.entities;

import lombok.*;

import javax.persistence.Table;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table
public class authResponse {
    private String token;

    private String welcome;

    private int id_client;

    private String message;

    private String errorMessage;

    private String firstname;

    private String cin;
}