package com.ampos.restaurant.gateway.domain;

import com.ampos.restaurant.gateway.enumeration.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Menu(Map<String, Object> record) {
        Map<String, Object> menuRecord = (Map<String, Object>) record.get("menu");
        this.id = (Long) menuRecord.get("id");
        this.name = (String) menuRecord.get("name");
        this.description = (String) menuRecord.getOrDefault("description", "");
        this.imageLink = (String) menuRecord.getOrDefault("imageLink", "");
        this.type = EnumUtils.getEnum(MenuType.class, (String) menuRecord.getOrDefault("type", MenuType.Unknow.toString()));
        this.price = (Long) menuRecord.getOrDefault("price", 0L);
    }
}

