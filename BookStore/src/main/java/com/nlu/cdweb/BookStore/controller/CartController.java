package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.CartItemRequest;
import com.nlu.cdweb.BookStore.dto.request.CartRequest;
import com.nlu.cdweb.BookStore.dto.response.CartItemResponse;
import com.nlu.cdweb.BookStore.dto.response.CartResponse;
import com.nlu.cdweb.BookStore.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        try{
            Page<CartResponse> responsePage = cartService.findAll(page, size);
            if(responsePage != null || !responsePage.isEmpty()){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successful Retrieval All Cart",responsePage));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Cart not found",responsePage));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Long id){
        try{
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("400", "Can not find Cartitem with negative id",""));
            }
            CartResponse response = cartService.findById(id);
            if(response != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successful Retrieval All CartItem By Id",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Cartitem not found",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> findByUserId(@RequestHeader("Authorization") String token){
        try{
            CartResponse response = cartService.findByUserId(token);
            if(response != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successful Retrieval All Cart By User Id",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Cart not found",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@RequestHeader("Authorization") String token, @RequestBody CartRequest request){
        try{
            CartResponse response = cartService.create(request, token);
            if(response != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("201", "Successful Created Cart",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Failed Create Cart",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@RequestHeader("Authorization") String token, @PathVariable Long id){
        try{
            boolean response = cartService.delete(token);
            if(response){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("204", "Successful Delete Cart",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Failed Delete Cart",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestHeader("Authorization") String token, @RequestBody CartRequest request){
        try{
            CartResponse response = cartService.update(request, token);
            if(response != null){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("202", "Successful Update CartItem",response));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404", "Failed Update CartItem",response));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("500", "Internal Server Error",e.getMessage()));
        }
    }
}
