package com.ampos.restaurant.gateway.service;

import com.ampos.restaurant.gateway.domain.Menu;

public interface MenuService {
    boolean createMenu(Menu menu);
    boolean updateMenu(Menu menu);

    boolean removeMenu(String menuName);
    Menu[] getMenus(int page, int size);
    Menu getMenuByName(String name);
    Menu[] searchMenu(String keyword);
}
