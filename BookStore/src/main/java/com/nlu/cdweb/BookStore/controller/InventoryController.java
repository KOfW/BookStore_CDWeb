package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.InventoryResponse;
import com.nlu.cdweb.BookStore.entity.InventoryEntity;
import com.nlu.cdweb.BookStore.services.IInventoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
