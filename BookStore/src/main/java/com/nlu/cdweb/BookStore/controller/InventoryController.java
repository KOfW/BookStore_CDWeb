package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.InventoryRequest;
import com.nlu.cdweb.BookStore.dto.response.BookResponse;
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

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> findAll(
            @Parameter(description = "The page number to retrieve", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "The number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size){
        try{
            Page<InventoryResponse> responses = inventoryService.findAll(page, size);
            if(responses != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successfull Retrieval of Inventory List",responses));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("404", "List of Inventory is empty",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Long id){
        try{
            InventoryResponse responses = inventoryService.findById(id);
            if(responses != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successfull Retrieval of Inventory By Id",responses));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("404", "Inventory is empty",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse> findByProductId(@PathVariable Long productId){
        try{
            InventoryResponse responses = inventoryService.findByProductId(productId);
            if(responses != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successfull Retrieval of Inventory By Product Id",responses));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("404", "Inventory is empty",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> create(@RequestBody InventoryRequest inventoryRequest){
        InventoryResponse addInventory = inventoryService.create(inventoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Add Book Succesfully",addInventory));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        try{
            boolean responses = inventoryService.delete(id);
            if(responses){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("204", "Successfull Deleted Inventory",responses));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("404", "List of Inventory is empty",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody InventoryRequest request){
        try{
            InventoryResponse responses = inventoryService.update(id, request);
            if(responses != null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("204", "Successfull Deleted Inventory",responses));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("404", "List of Inventory is empty",""));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",""));
        }
    }
}
