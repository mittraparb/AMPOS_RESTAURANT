package com.ampos.restaurant.gateway.controller;

import com.ampos.restaurant.gateway.domain.Bill;
import com.ampos.restaurant.gateway.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BillRestController {
    private final BillService billService;

    @Autowired
    public BillRestController(final BillService billService) {
        this.billService = billService;
    }

    @RequestMapping(value = "/api/bill",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = "application/json")
    public ResponseEntity createBill(@RequestBody Bill bill) {
        if (bill == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot create bill with null.");
        }
        boolean success = billService.createBill(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(success);
    }

    @RequestMapping(value = "/api/bill",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = "application/json")
    public ResponseEntity updateBill(@RequestBody Bill bill) {
        if (bill == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot create bill with null.");
        }
        boolean success = billService.updateBill(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(success);
    }

    @RequestMapping(value = "/api/bill/{buid}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity getBill(@PathVariable String buid) {
        Bill bill = billService.getBill(buid);
        return ResponseEntity.status(HttpStatus.OK).body(bill);
    }

    @RequestMapping(value = "/api/bills",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity getBills() {
        Bill[] bills = billService.getBills();
        return ResponseEntity.status(HttpStatus.OK).body(bills);
    }
}
