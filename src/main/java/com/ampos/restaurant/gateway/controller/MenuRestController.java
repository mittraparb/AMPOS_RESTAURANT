package com.ampos.restaurant.gateway.controller;

import com.ampos.restaurant.gateway.domain.Menu;
import com.ampos.restaurant.gateway.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    @Autowired
    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping(value = "/api/menu",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = "application/json")
    public ResponseEntity createMenu(@RequestBody Menu menu) {
        if (menu == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot create menu with null.");
        }
        menuService.createMenu(menu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
