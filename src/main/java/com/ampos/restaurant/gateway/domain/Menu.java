package com.ampos.restaurant.gateway.domain;

import com.ampos.restaurant.gateway.enumeration.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Menu {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String imageLink;
    private MenuType type;
    @Builder.Default
    private List<MenuTag> tags = new ArrayList<>();
}

