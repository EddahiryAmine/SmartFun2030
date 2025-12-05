package com.ace.ticketservice.repository;



import com.ace.ticketservice.model.CartItemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartItemRepository extends MongoRepository<CartItemEntity, String> {

    List<CartItemEntity> findByUserId(String userId);

    void deleteByUserId(String userId);
}
