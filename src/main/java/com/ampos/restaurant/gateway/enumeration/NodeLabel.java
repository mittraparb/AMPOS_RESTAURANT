package com.ampos.restaurant.gateway.enumeration;

import org.neo4j.graphdb.Label;

public enum NodeLabel implements Label {
    Menu, Tag, Bill, User, Privilege
}
