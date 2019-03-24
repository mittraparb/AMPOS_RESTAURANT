package com.ampos.restaurant.gateway.service.impl;

import com.ampos.restaurant.gateway.domain.Bill;
import com.ampos.restaurant.gateway.domain.Menu;
import com.ampos.restaurant.gateway.domain.OrderMenu;
import com.ampos.restaurant.gateway.enumeration.NodeLabel;
import com.ampos.restaurant.gateway.enumeration.RelType;
import com.ampos.restaurant.gateway.service.BillService;
import com.ampos.restaurant.gateway.service.DatabaseService;
import com.ampos.restaurant.gateway.service.MenuService;
import org.apache.commons.lang3.ArrayUtils;
import org.neo4j.graphdb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class BillServiceImpl implements BillService {
    private final DatabaseService databaseService;
    private final MenuService menuService;

    @Autowired
    public BillServiceImpl(final DatabaseService databaseService,
                           final MenuService menuService) {
        this.databaseService = databaseService;
        this.menuService = menuService;
    }

    @Override
    public boolean createBill(Bill bill) {
        GraphDatabaseService graphDb = databaseService.getGraphDatabaseService();
        boolean success = false;
        try (Transaction tx = graphDb.beginTx()) {
            bill.setBuid(UUID.randomUUID().toString());
            Node billNode = graphDb.findNode(NodeLabel.Bill, "buid", bill.getBuid());
            if (billNode == null) {
                billNode = graphDb.createNode(NodeLabel.Bill);
                billNode.setProperty("buid", bill.getBuid());
                createOrderRelation(graphDb, billNode, bill.getOrderMenus());
            }
            success = true;
            tx.success();
        }

        return success;
    }

    @Override
    public boolean updateBill(Bill bill) {
        GraphDatabaseService graphDb = databaseService.getGraphDatabaseService();
        boolean success = false;
        try (Transaction tx = graphDb.beginTx()) {
            Node billNode = graphDb.findNode(NodeLabel.Bill, "buid", bill.getBuid());
            if (billNode != null) {
                billNode.getRelationships(RelType.HAS_ORDERED)
                        .iterator().forEachRemaining(r -> r.delete());
                createOrderRelation(graphDb, billNode, bill.getOrderMenus());
            }
            success = true;
            tx.success();
        }

        return success;
    }

    private void createOrderRelation(GraphDatabaseService graphDb,
                                     Node billNode,
                                     List<OrderMenu> orderMenus) {
        double totalPrice = 0;
        for (OrderMenu orderMenu : orderMenus) {
            String menuName = orderMenu.getMenuName();
            int quantity = orderMenu.getQuantity();
            Node nodeMenu = graphDb.findNode(NodeLabel.Menu, "name", menuName);
            if (nodeMenu != null) {
                long price = (Long) nodeMenu.getProperty("price");
                totalPrice += price * quantity;

                Relationship orderRel = billNode.createRelationshipTo(nodeMenu, RelType.HAS_ORDERED);
                orderRel.setProperty("quantity", quantity);
                orderRel.setProperty("orderedTime", Instant.now().toEpochMilli());
            }
        }
        billNode.setProperty("totalPrice", totalPrice);
    }

    @Override
    public boolean removeBill(String buid) {
        return false;
    }

    @Override
    public Bill getBill(String buid) {
        String query = "match (bill:Bill{buid: $buid})-[ordered:HAS_ORDERED]->(menu:Menu) \n" +
                "with bill, collect({menuName: menu.name, quantity: ordered.quantity, orderedTime: ordered.orderedTime}) as orderMenus\n" +
                "return bill{.*}, orderMenus";
        Map<String, Object> params = new HashMap<>();
        params.put("buid", buid);

        Function<Result, Bill> mapping = result -> (result.hasNext()) ?
                result.map(Bill::new).next() :
                null;
        return databaseService.executeSingle(query, params, mapping);
    }

    @Override
    public Bill[] getBills() {
        String query = "match (bill:Bill)-[ordered:HAS_ORDERED]->(menu:Menu) \n" +
                "with bill, collect({menuName: menu.name, quantity: ordered.quantity, orderedTime: ordered.orderedTime}) as orderMenus\n" +
                "return bill{.*}, orderMenus";

        Function<Result, Bill[]> mapping = result -> result.map(Bill::new).stream().toArray(Bill[]::new);
        return ArrayUtils.nullToEmpty(databaseService.executeSingle(query, mapping), Bill[].class);
    }
}
