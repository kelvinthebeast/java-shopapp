package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.*;

//@Validated
@RestController
@RequestMapping("api/v1/categories")
//@Validated
public class CategoryController {
    @GetMapping("")  //http://localhost:8080/api/v1/categories
    public ResponseEntity<String> getCategories() {
        return ResponseEntity.ok("Categories");
    }

    @PostMapping("")
    public ResponseEntity<String> addCategory() {
        return ResponseEntity.ok("category added");
    }

    @PatchMapping("")
    public ResponseEntity<String> updateCategories() {
        return ResponseEntity.ok("This is updateCategories");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable long id) {
        return ResponseEntity.ok("This is deleteCategories id " + id);
    }

    @GetMapping("/test")
    public ResponseEntity<String> getCategoriesByParams(@RequestParam int limit, @RequestParam int page) {
        return ResponseEntity.ok(String.format("This is categories by pams with limit=%d, page=%d", limit, page));
    }

    @Valid
    @PostMapping("/test")
    public ResponseEntity<String> postCategoriesDto(
            @RequestBody CategoryDTO checkDTO,
            BindingResult result
    ) {


        return ResponseEntity.ok("check dto " + checkDTO);
    }

    @PostMapping("/test/v1")
    public ResponseEntity<?> postCategoriesDtoV1(@Valid @RequestBody CategoryDTO checkDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorsMessages = result.getFieldErrors()
                                                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorsMessages.toString());
        }
        return ResponseEntity.ok("check dto v1 " + checkDTO);
    }
}
