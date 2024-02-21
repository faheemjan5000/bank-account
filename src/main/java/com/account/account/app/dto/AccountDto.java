package com.account.account.app.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Integer id;
    @NotBlank(message = "Account holder name should not be empty")
    private String accountHolderName;
    @Min(value = 10,message = "Minimum balance should be 10")
    private double balance;
    //@Min(value = 14,message = "IBAN number should be 14 digits")
    //@Max(value = 14,message = "IBAN number should be 14 digits")
    @Size(min = 14,max = 14,message = "IBAN number should be 14")
    private String iban;

}
