package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products") // Base URL cho tất cả các endpoint trong controller này
public class ProductController {

    @GetMapping("")
    public ResponseEntity<String> getProducts(@RequestParam int page, @RequestParam int limit) {
        // Trả về thông tin các sản phẩm dựa trên trang (page) và số lượng giới hạn (limit)
        return ResponseEntity.ok(String.format("Get all product here with page = %d, limit = %d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProduct(@PathVariable("id") int id) {
        // Trả về thông tin chi tiết của một sản phẩm dựa trên ID
        return ResponseEntity.ok(String.format("Product with id %d", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int id) {
        // Xóa một sản phẩm dựa trên ID
        return ResponseEntity.ok(String.format("Delete product with id %d", id));
    }

    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult result) {
        try {
            // Kiểm tra xem có lỗi trong dữ liệu được gửi từ client không
            if (result.hasErrors()) {
                // Lấy tất cả các thông báo lỗi và trả về
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            List<MultipartFile> files = productDTO.getFiles(); // Lấy file từ đối tượng ProductDTO


            files = files == null ? new ArrayList<MultipartFile>() : files;
            for (MultipartFile file: files) {


                // kiêm tra kích thước file == 0
                if (file.getSize() == 0) continue;
                // Kiểm tra kích thước file (giới hạn 10 MB)
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum is 10 MB");
                }

                // Kiểm tra loại file, chỉ chấp nhận file ảnh
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image")) {
                    return ResponseEntity.badRequest().body("File is not an image");
                }

                // Lưu file và lấy tên file đã được xử lý
                String fileName = storeFile(file);
                productDTO.setThumbnail(fileName); // Thiết lập đường dẫn thumbnail cho sản phẩm
            }
        } catch (Exception e) {
            // Xử lý các ngoại lệ xảy ra và trả về thông báo lỗi
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // Nếu không có lỗi, trả về thông báo thành công
        return ResponseEntity.ok("Product created");
    }

    private String storeFile(MultipartFile file) throws IOException {
        // Lấy tên file gốc từ file được upload
        String fileName = StringUtils.getFilename(file.getOriginalFilename());
        // Tạo tên file duy nhất bằng cách thêm UUID trước tên file
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        // Xác định thư mục lưu trữ file
        Path uploadDir = Paths.get("uploads");
        // Kiểm tra xem thư mục đã tồn tại chưa, nếu chưa thì tạo mới
        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }

        // Tạo đường dẫn đầy đủ cho file cần lưu
        Path destination = uploadDir.resolve(uniqueFileName);
        // Sao chép file từ input stream vào thư mục đích, thay thế nếu file đã tồn tại
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        // Trả về tên file duy nhất đã được tạo
        return uniqueFileName;
    }
}
//{
//        "name": "Sample Product",
//        "price": 4999.99,
//        "thumbnail": "https://example.com/image.jpg",
//        "description": "This is a sample product description.",
//        "category_id": "12345"
//        }
