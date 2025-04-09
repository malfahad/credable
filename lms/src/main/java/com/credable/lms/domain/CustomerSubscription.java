package com.credable.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "customer_subscription")
public class CustomerSubscription {
    @Id
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "subscription_date", nullable = false)
    private LocalDateTime subscriptionDate;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
} 