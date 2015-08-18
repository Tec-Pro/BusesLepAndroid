package com.tecpro.buseslep.utils;

import java.math.BigDecimal;

/**
 * Created by nico on 27/06/15.
 */
public class PaymentTecPro {

    private String description;
    private String externalReference;
    private Integer installments;
    private PayerTecPro payer;
    private String paymentMethodId;
    private String token;
    private BigDecimal transactionAmount;

    public PaymentTecPro(String description, String externalReference, Integer installments, String email, String paymentMethodId, String token, BigDecimal transactionAmount) {
        this.description = description;
        this.externalReference = externalReference;
        this.installments = installments;
        this.payer = new PayerTecPro(email);
        this.paymentMethodId = paymentMethodId;
        this.token = token;
        this.transactionAmount = transactionAmount;
    }
}
