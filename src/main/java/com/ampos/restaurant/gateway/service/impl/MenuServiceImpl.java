package com.ampos.restaurant.gateway.service.impl;

import com.ampos.restaurant.gateway.domain.Menu;
import com.ampos.restaurant.gateway.domain.MenuTag;
import com.ampos.restaurant.gateway.enumeration.NodeLabel;
import com.ampos.restaurant.gateway.enumeration.RelType;
import com.ampos.restaurant.gateway.service.DatabaseService;
import com.ampos.restaurant.gateway.service.MenuService;
import org.apache.commons.lang3.ArrayUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class MenuServiceImpl implements MenuService {
    private DatabaseService databaseService;

    @Autowired
    public MenuServiceImpl(final DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public boolean createMenu(Menu menu) {
        boolean created = true;
        GraphDatabaseService graphDb = databaseService.getGraphDatabaseService();
        try (Transaction tx = graphDb.beginTx()) {
            List<MenuTag> meuTags = menu.getTags();
            Node menuNode = graphDb.findNode(NodeLabel.Menu, "name", menu.getName());
            if (menuNode == null) {
                menuNode = graphDb.createNode(NodeLabel.Menu);
                menuNode.setProperty("name", menu.getName());
                menuNode.setProperty("price", menu.getPrice());
                menuNode.setProperty("description", menu.getDescription());
                menuNode.setProperty("imageLink", menu.getImageLink());
                menuNode.setProperty("type", menu.getType().toString());

                for (MenuTag menuTag : meuTags) {
                    Node menuTagNode = graphDb.findNode(NodeLabel.Tag, "name", menuTag.getName());
                    if (menuTagNode == null) {
                        menuTagNode = graphDb.createNode(NodeLabel.Tag);
                        menuTagNode.setProperty("name", menuTag.getName());
                    }
                    menuNode.createRelationshipTo(menuTagNode, RelType.TAGGED_IN);
                }
            } else {
                created = false;
            }
            tx.success();
        }
        return created;
    }

    @Override
    public boolean updateMenu(Menu menu) {
        boolean created = true;
        GraphDatabaseService graphDb = databaseService.getGraphDatabaseService();
        try (Transaction tx = graphDb.beginTx()) {
            List<MenuTag> meuTags = menu.getTags();
            Node menuNode = graphDb.findNode(NodeLabel.Menu, "name", menu.getName());
            if (menuNode != null) {
                menuNode.setProperty("name", menu.getName());
                menuNode.setProperty("price", menu.getPrice());
                menuNode.setProperty("description", menu.getDescription());
                menuNode.setProperty("imageLink", menu.getImageLink());
                menuNode.setProperty("type", menu.getType().toString());

                //Remove all relation
                menuNode.getRelationships(RelType.TAGGED_IN)
                        .forEach(r -> r.delete());
                for (MenuTag menuTag : meuTags) {
                    Node menuTagNode = graphDb.findNode(NodeLabel.Tag, "name", menuTag.getName());
                    if (menuTagNode == null) {
                        menuTagNode = graphDb.createNode(NodeLabel.Tag);
                        menuTagNode.setProperty("name", menuTag.getName());
                    }
                    menuNode.createRelationshipTo(menuTagNode, RelType.TAGGED_IN);
                }
            } else {
                created = false;
            }
            tx.success();
        }
        return created;
    }

    @Override
    public boolean removeMenu(String menuName) {
        boolean removed = false;
        GraphDatabaseService graphDb = databaseService.getGraphDatabaseService();
        try (Transaction tx = graphDb.beginTx()) {
            Node menuNode = graphDb.findNode(NodeLabel.Menu, "name", menuName);
            if (menuNode != null) {
                menuNode.delete();
                removed = true;
            }

            tx.success();
        }
        return removed;
    }

    @Override
    public Menu[] getMenus(int page, int size) {
        String query =
                "MATCH (menu:Menu) " +
                        "RETURN menu{.*} " +
                        "ORDER BY menu.name " +
                        "SKIP $page LIMIT $size";
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);

        Function<Result, Menu[]> mapping = result ->
                result.map(Menu::new).stream().toArray(Menu[]::new);

        return ArrayUtils.nullToEmpty(databaseService.execute(query, params, mapping), Menu[].class);
    }

    @Override
    public Menu getMenuByName(String name) {
        String query = "MATCH (menu:Menu{name: $name}) RETURN menu{.*}";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        Function<Result, Menu> mapping = result -> (result.hasNext()) ?
                result.map(Menu::new).next() :
                null;
        return databaseService.executeSingle(query, params, mapping);
    }

    @Override
    public Menu[] searchMenu(String keyword) {
        keyword = String.format("(?i).*%s.*", keyword);
        String query = "match (menu:Menu)-[tagged:TAGGED_IN]->(menuTag:Tag) " +
                "where menu.name =~ $keyword " +
                "or menu.description =~ $keyword " +
                "or menuTag.name =~ $keyword " +
                "return distinct menu{.*}";
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);

        Function<Result, Menu[]> mapping = result ->
                result.map(Menu::new).stream().toArray(Menu[]::new);

        return ArrayUtils.nullToEmpty(databaseService.execute(query, params, mapping), Menu[].class);
    }
}
