package com.tw.bootcamp.bookshop.purchase;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderAddressDTO {
    @NotBlank(message = "lineNoOne can not be blank")
    private String lineNoOne;
    private String lineNoTwo;
    @NotBlank(message = "city can not be blank")
    private String city;
    @NotBlank(message = "state can not be blank")
    private String state;
    @NotBlank(message = "pinCode can not be blank")
    @Pattern(regexp="^[1-9][0-9]{5}$",message = "Please provide a valid pin code")
    private String pinCode;
    @NotBlank(message = "country name can not be blank")
    private String country;
}
