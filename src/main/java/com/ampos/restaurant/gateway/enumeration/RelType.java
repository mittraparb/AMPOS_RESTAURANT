package com.ampos.restaurant.gateway.enumeration;

import org.neo4j.graphdb.RelationshipType;

public enum RelType implements RelationshipType {
    HAS_PRIVILEGE, HAS_ITEM, HAS_ORDERED, TAGGED_IN
}
