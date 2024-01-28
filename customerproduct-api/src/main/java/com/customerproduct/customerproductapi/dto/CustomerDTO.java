package com.customerproduct.customerproductapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "First name should not be empty")
    private String firstName;

    @NotBlank(message = "Last name should not be empty")
    private String lastName;

    @Email(message = "Should be a valid email")
    @NotBlank(message = "Email should not be empty")
    private String email;

    @NotBlank(message = "Phone number should not be empty")
    private String phoneNumber;

    private int familyMembers;

    public void setFamilyMembers(int familyMembers) {
        this.familyMembers = familyMembers;
    }
}
