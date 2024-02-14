package com.account.account.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Integer id;
    @NotBlank(message = "Account holder name should not be empty")
    private String accountHolderName;
    @Min(value = 10,message = "Minimum balance should be 10")
    private double balance;

}
