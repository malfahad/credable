package com.credable.lms.domain;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreditReport {
    private int creditScore;
    private BigDecimal limitAmount;
    private String exclusion;
    private String exclusionReason;
 
    public CreditReport(int creditScore, BigDecimal limitAmount, String exclusion, String exclusionReason) {
        this.creditScore = creditScore;
        this.limitAmount = limitAmount;
        this.exclusion = exclusion;
        this.exclusionReason = exclusionReason;
    }
} 