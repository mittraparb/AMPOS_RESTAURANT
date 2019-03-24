package com.ampos.restaurant.gateway.controller;

import com.ampos.restaurant.gateway.domain.Menu;
import com.ampos.restaurant.gateway.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        boolean success = menuService.createMenu(menu);
        return ResponseEntity.status(HttpStatus.CREATED).body(success);
    }

    @RequestMapping(value = "/api/menu",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = "application/json")
    public ResponseEntity updateMenu(@RequestBody Menu menu) {
        if (menu == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot edit menu with null.");
        }
        boolean success = menuService.updateMenu(menu);
        return ResponseEntity.status(HttpStatus.CREATED).body(success);
    }

    @RequestMapping(value = "/api/menu/{menuName}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    public ResponseEntity removeMenu(@PathVariable String menuName) {
        boolean success = menuService.removeMenu(menuName);
        return ResponseEntity.status(HttpStatus.CREATED).body(success);
    }

    @RequestMapping(value = "/api/menu/page/{page}/size/{size}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity getMenu(@PathVariable Integer page,
                                     @PathVariable Integer size) {
        Menu[] menus = menuService.getMenus(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(menus);
    }

    @RequestMapping(value = "/api/menu/search/{keyword}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity getMenu(@PathVariable String keyword) {
        Menu[] menus = menuService.searchMenu(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(menus);
    }
}
