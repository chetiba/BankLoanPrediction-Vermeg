package com.vermeg.chtiba.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class authRequest {
    private String email;

    private String pwd;
}