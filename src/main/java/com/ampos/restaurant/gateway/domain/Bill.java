package com.ampos.restaurant.gateway.domain;

import com.ampos.restaurant.gateway.enumeration.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class Bill {
    private String buid;
    @Builder.Default
    private List<OrderMenu> orderMenus = new ArrayList<>();
    private double totalPrice;

    public Bill(Map<String, Object> record) {
        Map<String, Object> billRecord = (Map<String, Object>) record.get("bill");
        this.buid = (String) billRecord.get("buid");
        this.totalPrice = (Double) billRecord.getOrDefault("totalPrice", 0);
        this.orderMenus = (List<OrderMenu>) record.get("orderMenus");
    }
}
