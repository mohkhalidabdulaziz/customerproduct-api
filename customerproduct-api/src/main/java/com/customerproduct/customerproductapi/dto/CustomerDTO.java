package com.customerproduct.customerproductapi.dto;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private List<CustomerDTO> familyMembers;

    public void setFamilyMembers(List<CustomerDTO> familyMembers) {
        this.familyMembers = familyMembers;
    }
}
