package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import com.nlu.cdweb.BookStore.entity.InventoryEntity;
import com.nlu.cdweb.BookStore.services.IInventoryService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/inventories")
public class InventoryController {
    @Autowired
    private final IInventoryService inventoryService;
    @PostMapping("")
    public ResponseEntity<ApiResponse> create(@RequestBody InventoryRequest inventoryRequest){
        InventoryResponse addInventory = inventoryService.create(inventoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Add Book Succesfully",addInventory));
    }
    @GetMapping("/getAll")
    public ResponseEntity<Page<InventoryResponse>> findAll(
            @Parameter(description = "The page number to retrieve", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "The number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size){
        var result = inventoryService.findAll(page, size);
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(result);
    }
    
}
