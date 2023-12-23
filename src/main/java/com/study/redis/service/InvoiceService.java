package com.study.redis.service;

import com.study.redis.domian.Invoice;

import java.util.List;

public interface InvoiceService {
     Invoice saveInvoice(Invoice invoice);

     Invoice updateInvoice(Invoice invoice,  Long invoiceId);

     void deleteInvoice( Long invoiceId);

     Invoice getInvoiceById( Long invoiceId);

     List<Invoice> getAllInvoices();
}
