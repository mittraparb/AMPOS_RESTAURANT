package com.ampos.restaurant.gateway.service;

import com.ampos.restaurant.gateway.domain.Bill;

public interface BillService {
    boolean createBill(Bill bill);
    boolean updateBill(Bill bill);
    boolean removeBill(String buid);

    Bill getBill(String buid);
    Bill[] getBills();
}
