package eu.mondo.map.core.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;

public class Node<T> {

	protected T object;
	protected ListMultimap<String, Node<T>> dimensions;
	protected ListMultimap<Node<T>, String> incomingNeighbors;
	protected ListMultimap<Node<T>, String> outgoingNeighbors;

	public Node(T object) {
		this.object = object;
		dimensions = ArrayListMultimap.create();
		incomingNeighbors = ArrayListMultimap.create();
		outgoingNeighbors = ArrayListMultimap.create();
	}

	public int getNumberOfNeighbors(final String dimension) {
		return getNeighbors(dimension).size();
	}

	public int getNumberOfNeighbors() {
		return incomingNeighbors.size() + outgoingNeighbors.size();
	}

	public int getNumberOfDisjunctNeighbors() {
		return getNeighbors().size();
	}

	public List<Node<T>> getNeighbors(final String dimension) {
		return dimensions.get(dimension);
	}

	public int getNumberOfDimensions() {
		return dimensions.size();
	}

	public List<Node<T>> getNeighbors() {
		List<Node<T>> neighbors = new ArrayList<Node<T>>();
		neighbors.addAll(incomingNeighbors.keySet());
		neighbors.addAll(outgoingNeighbors.keySet());
		return neighbors;
	}

	public Set<Node<T>> getDisjunctNeighbors() {
		List<Node<T>> neighbors = new ArrayList<Node<T>>();
		neighbors.addAll(incomingNeighbors.keySet());
		neighbors.addAll(outgoingNeighbors.keySet());
		return ImmutableSet.copyOf(neighbors);
	}

	public boolean hasNeighbor(final Node<?> neighbor2) {
		return incomingNeighbors.containsKey(neighbor2) || outgoingNeighbors.containsKey(neighbor2);
	}

	public boolean hasIncomingNeighbor(final Node<T> node) {
		return incomingNeighbors.containsKey(node);
	}

	public boolean hasOutgoingNeighbor(final Node<T> node) {
		return outgoingNeighbors.containsKey(node);
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public ListMultimap<String, Node<T>> getDimensions() {
		return dimensions;
	}

	public Set<String> getDimensionsAsSet() {
		return dimensions.keySet();
	}

	public void setDimensions(ListMultimap<String, Node<T>> dimensions) {
		this.dimensions = dimensions;
	}

	public ListMultimap<Node<T>, String> getIncomingNeighbors() {
		return incomingNeighbors;
	}

	public void setIncomingNeighbors(ListMultimap<Node<T>, String> incomingNeighbors) {
		this.incomingNeighbors = incomingNeighbors;
	}

	public ListMultimap<Node<T>, String> getOutgoingNeighbors() {
		return outgoingNeighbors;
	}

	public void setOutgoingNeighbors(ListMultimap<Node<T>, String> outgoingNeighbors) {
		this.outgoingNeighbors = outgoingNeighbors;
	}

	public int numberOfDimensions() {
		return dimensions.size();
	}

	public boolean hasDimension(String dimension) {
		return dimensions.containsKey(dimension);
	}

	public boolean addDimension(String dimension, final Node<T> node) {
		return dimensions.put(dimension, node);
	}

	public int getDegree() {
		return incomingNeighbors.size() + outgoingNeighbors.size();
	}

	public void addOutgoingNeighbor(final Node<T> node, final String dimension) {
		outgoingNeighbors.put(node, dimension);
	}

	public void addIncomingNeighbor(final Node<T> node, final String dimension) {
		incomingNeighbors.put(node, dimension);
	}

	@Override
	public String toString() {
		return "Node [object=" + object + "]";
	}

}
