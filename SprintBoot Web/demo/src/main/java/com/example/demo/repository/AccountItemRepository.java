package com.example.demo.repository;

import com.example.demo.entities.AccountItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountItemRepository extends JpaRepository<AccountItem, Long> {
	List<AccountItem> findByHotelServiceId(Long hotelServiceId);
	List<AccountItem> findByAccountId(Long accountId);
	List<AccountItem> findByAccountIdAndEliminadoFalse(Long accountId);
	List<AccountItem> findByAccountIdAndEliminadoTrue(Long accountId);
}
