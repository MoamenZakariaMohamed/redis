package com.study.redis;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice")
@Builder
public class Invoice implements Serializable {

    private static final long serialVersionUID = -6519414420495229784L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "amount")
    private Double amount;

    public  static  Invoice of(InvoiceDTO invoiceDTO){
        return Invoice.builder().name(invoiceDTO.getName()).amount(invoiceDTO.getAmount()).build();
    }
}