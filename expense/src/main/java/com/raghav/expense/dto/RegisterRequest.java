package com.raghav.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter 
@NoArgsConstructor 
@AllArgsConstructor 
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

}
