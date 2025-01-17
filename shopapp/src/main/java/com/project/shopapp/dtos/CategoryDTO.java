package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    @NotEmpty(message = "Category can't not be empty")
    private String name;

//    @NotEmpty(message = "Category can't not be empty")
//    public String getName() {
//        return this.name;
//    }

}
