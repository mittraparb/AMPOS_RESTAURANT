package com.ampos.restaurant.gateway.service.impl;

import com.ampos.restaurant.gateway.domain.Menu;
import com.ampos.restaurant.gateway.domain.MenuTag;
import com.ampos.restaurant.gateway.enumeration.MenuType;
import com.ampos.restaurant.gateway.enumeration.NodeLabel;
import com.ampos.restaurant.gateway.enumeration.RelType;
import com.ampos.restaurant.gateway.service.DatabaseService;
import com.ampos.restaurant.gateway.service.MenuService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            MenuType type = menu.getType();
            List<MenuTag> meuTags = menu.getTags();
            Node menuNode = graphDb.findNode(type, "name", menu.getName());
            if (menuNode == null) {
                menuNode = graphDb.createNode( (type != null) ? type : NodeLabel.Menu);
                menuNode.setProperty("name", menu.getName());
                menuNode.setProperty("price", menu.getPrice());
                menuNode.setProperty("description", menu.getDescription());
                menuNode.setProperty("imageLink", menu.getImageLink());

                for (MenuTag menuTag : meuTags) {
                    Node menuTagNode = graphDb.findNode(NodeLabel.Tag, "name", menuTag.getName());
                    if (menuTagNode == null) {
                        menuTagNode = graphDb.createNode(NodeLabel.Tag);
                        menuTagNode.setProperty("name", menuTag.getName());
                    }
                    menuNode.createRelationshipTo(menuTagNode, RelType.TAGGED_IN);
                }
            }
            else {
                created = false;
            }
            tx.success();
        }
        return created;
    }
}
