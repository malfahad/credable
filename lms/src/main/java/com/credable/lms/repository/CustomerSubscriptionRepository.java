package com.credable.lms.repository;

import com.credable.lms.domain.CustomerSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSubscriptionRepository extends JpaRepository<CustomerSubscription, Long> {
    boolean existsByCustomerId(Long customerId);
} 