package com.vermeg.chtiba.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
    public class registerRequest {
        private String email;

        private String pwd;

        private String firstname;

        private String lastname;

        private String cin;

        private String address;

        private String phonenumber;
}
