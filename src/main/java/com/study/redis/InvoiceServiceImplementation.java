package com.study.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImplementation implements InvoiceService{

    private final InvoiceRepository invoiceRepo;
    @Override
    @CachePut(value = "invoices", key = "#invoice.id")
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepo.save(invoice);
    }
    @Override
    @CachePut(value = "Invoice", key = "#invoiceid")
    public Invoice updateInvoice(Invoice inv, Long invoiceid) {
        Invoice invoice = invoiceRepo.findById(invoiceid)
                .orElseThrow(() -> new RuntimeException("Invoice Not Found"));
        invoice.setAmount(inv.getAmount());
        invoice.setName(inv.getName());
        return invoiceRepo.save(invoice);
    }

    @Override
    @CacheEvict(value = "Invoice", key = "#invoiceid")
    // @CacheEvict(value="Invoice", allEntries=true) //in case there are multiple records to delete
    public void deleteInvoice(Long invoiceid) {
        Invoice invoice = invoiceRepo.findById(invoiceid)
                .orElseThrow(() -> new RuntimeException("Invoice Not Found"));
        invoiceRepo.delete(invoice);
    }

    @Override
    @Cacheable(value = "Invoice", key = "#invoiceid")
    public Invoice getInvoiceById(Long invoiceid) {
        return invoiceRepo.findById(invoiceid).orElseThrow(() -> new RuntimeException("Invoice Not Found"));
    }

    @Override
    @Cacheable(value = "Invoice")
    public List<Invoice> getAllInvoices() {
        return invoiceRepo.findAll();
    }
}
