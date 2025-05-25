package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.client.InventoryApiClient;
import com.nlu.cdweb.BookStore.client.ProductApiClient;
import com.nlu.cdweb.BookStore.dto.response.BookResponse;
import com.nlu.cdweb.BookStore.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductAvaiability {
    @Autowired
    ProductApiClient client;
    @Autowired
    InventoryApiClient invClient;

    public boolean checkAvaiability(Map<Long, Integer> item){

        Set<Long> desiredProductsIds = item.keySet();
        List<BookResponse> availableProducts = client.findAllByIds(desiredProductsIds);
        if(availableProducts.size()==item.keySet().size()){
            boolean isQuantityValid = desiredProductsIds.stream()
                    .allMatch(productId ->
                            invClient.findInventoryForProductid(productId).getQuantity() >= item.get(productId));

            return isQuantityValid;
        }else{
            throw new EntityNotFoundException("At least one products isn't found");
        }

    }
}
