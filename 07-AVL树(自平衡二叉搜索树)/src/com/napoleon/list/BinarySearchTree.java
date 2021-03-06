package com.napoleon.list;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import com.napoleon.printer.BinaryTreeInfo;

@SuppressWarnings("unchecked")
public class BinarySearchTree<E> extends BinaryTree<E> {

	private Comparator<E> comparator;

	public BinarySearchTree() {
		// TODO Auto-generated constructor stub
		this(null);
	}

	public BinarySearchTree(Comparator<E> comparator) {
		this.comparator = comparator;
	}

	public void add(E element) {
		elementNotNullCheck(element);
		if (root == null) {
			root = createNode(element, null);
			size++;
			afterAdd(root);
			return;
		}

//		找到父节点
		Node<E> parent = root;
		int cmp = 0;
		Node<E> node = root;
		while (node != null) {
			cmp = compare(element, node.element);
			parent = node;
			if (cmp > 0) {
				node = node.right;
			} else if (cmp < 0) {
				node = node.left;
			} else { // 相等
//				覆盖原来的值	
				node.element = element;
				return;
			}
		}
//		看看插入到父节点的哪个位置
		Node<E> newNode = createNode(element, parent);
		if (cmp > 0) {
			parent.right = newNode;
		} else {
			parent.left = newNode;
		}
		size++;
		afterAdd(newNode);
	}

	/**
	 * 是否包含某元素
	 * 
	 * @param element
	 * @return
	 */
	public boolean contains(E element) {
		return node(element) != null;
	}

	public void remove(E element) {
		remove(node(element));
	}

	/**
	 * 添加node之后的调整
	 */
	protected void afterAdd(Node<E> node) {
	}

	/**
	 * 删除node之后的调整
	 */
	protected void afterRemove(Node<E> node) {
	}
	/**
	 * 删除节点
	 * 
	 * @param node
	 */
	private void remove(Node<E> node) {
		if (node == null) {
			return;
		}
//		删除度为2的节点
		if (node.hasTwoChildren()) {
			Node<E> s = successor(node);
			// 把后继节点的值赋值给被删除的节点
			node.element = s.element;
			// 删除后继节点（度为2的节点的后继节点的度必定为1或者0）
			node = s;
		}
		// 删除node节点（node的度必然是1或者0）
		Node<E> replacement = node.left != null ? node.left : node.right;
		if (replacement != null) { // node是度为1的节点
			// 更改parent
			replacement.parent = node.parent;
			// 更改parent的left、right的指向
			if (node.parent == null) { // node是度为1的节点并且是根节点
				root = replacement;
			} else if (node == node.parent.left) {
				node.parent.left = replacement;
			} else { // node == node.parent.right
				node.parent.right = replacement;
			}

			// 删除节点之后的处理
			afterRemove(node);
		} else if (node.parent == null) { // node是叶子节点并且是根节点
			root = null;

			// 删除节点之后的处理
			afterRemove(node);
		} else { // node是叶子节点，但不是根节点
			if (node == node.parent.left) {
				node.parent.left = null;
			} else { // node == node.parent.right
				node.parent.right = null;
			}

			// 删除节点之后的处理
			afterRemove(node);
		}
	}

	private Node<E> node(E element) {
		Node<E> node = root;
		while (node != null) {
			int cmp = compare(element, node.element);
			if (cmp == 0) {
				return node;
			}
			if (cmp > 0) {
				node = node.right;
			} else {
				node = node.left;
			}
		}
		return null;
	}

	/**
	 * 元素比较逻辑
	 * 
	 * @param e1
	 * @param e2
	 * @return 0: e2 = e2, 1: e1> e2, 2: e2 > e1
	 */
	private int compare(E e1, E e2) {
//		设计相关的问题： 如果外界想个性化定制比较逻辑，就传一个比较器；否则，就实现Comparable接口。
		if (comparator != null) {
			return comparator.compare(e1, e2);
		}
		return ((Comparable<E>) e1).compareTo(e2);
	}

	private void elementNotNullCheck(E element) {
		if (element == null) {
			throw new IllegalArgumentException("element must not be null");
		}
	}
}
