package com.example.controller;

import com.example.model.Category;
import com.example.service.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    void Hello() {System.out.println("eii from admincategoryct");}
    private final AdminCategoryService adminCategoryService;

    @GetMapping
    public ResponseEntity<List<Category>> listCategories(){
        return ResponseEntity.ok(adminCategoryService.getAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Category>> pageCategories(
            @PageableDefault(size = 5, sort = "name") Pageable pageable){
        Page<Category> categories = adminCategoryService.paginate(pageable);
        return new ResponseEntity<Page<Category>>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable ("id") Integer id){
        Category category = adminCategoryService.findById(id);
        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> createCategoryById(@RequestBody Category category){
        Category newCategory = adminCategoryService.create(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") Integer id,
                                                   @RequestBody Category category){
        Category updateCategory = adminCategoryService.update(id,category);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategoryById(@PathVariable ("id") Integer id){
        adminCategoryService.delete(id);
        return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
    }
}
