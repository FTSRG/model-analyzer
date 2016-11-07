package hu.bme.mit.mba.modeladapters.neo4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;

import hu.bme.mit.mba.modeladapters.ModelIndexer;
import hu.bme.mit.mba.modeladapters.TypedModelAdapter;

public class Neo4jModelAdapter extends TypedModelAdapter<Node, String> {

	GraphDatabaseService graph;

    @Override
    public Iterator<Node> getModelIterator() {
        return graph.getAllNodes().iterator();
    }

    public void init(GraphDatabaseService graph) {
        this.graph = graph;
        init(graph.getAllNodes());
    }

    protected void init(ResourceIterable<Node> nodes) {
    	indexer = new ModelIndexer<Node, String>();

        for (Node node : nodes) {
        	for (Relationship relationship: node.getRelationships(Direction.OUTGOING)) {
        		Node neighbor = relationship.getOtherNode(node);
        		addEdge(node, relationship.getType(), neighbor);
        	}
        }
    }

    protected List<Node> getNeighbors(final Node object, final RelationshipType relationshipType) {
        return new ArrayList<>(); // TODO
    }

    protected void addEdge(final Node node, final RelationshipType relationshipType, final Node neighbor) {
        if (neighbor != null && relationshipType != null) {
            node.createRelationshipTo(neighbor, relationshipType);
        }
    }

}