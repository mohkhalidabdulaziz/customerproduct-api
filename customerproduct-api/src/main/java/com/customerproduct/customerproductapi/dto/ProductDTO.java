package com.customerproduct.customerproductapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Book title should not be empty")
    private String bookTitle;

    @NotNull(message = "Book price should not be empty")
    private double bookPrice;

    private int bookQuantity;
}
