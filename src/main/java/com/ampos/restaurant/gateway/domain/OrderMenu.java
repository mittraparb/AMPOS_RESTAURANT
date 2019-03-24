package com.ampos.restaurant.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderMenu {
    String menuName;
    int quantity;
    long orderedTime;
}
