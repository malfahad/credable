package com.credable.lms.repository;

import com.credable.lms.domain.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.credable.lms.domain.LoanStatus;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    List<LoanRequest> findByCustomerId(Long customerId);
    Optional<LoanRequest> findFirstByCustomerIdOrderByRequestDateDesc(Long customerId);
    boolean existsByCustomerIdAndStatus(Long customerId, LoanStatus status);
} 