package com.hqhe;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Problem_146_LRUCache {
    /*
    LRU : least recently used 选择最近最少被访问的数据进行淘汰
    请你设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。
    实现 LRUCache 类：
    LRUCache(int capacity) 以 正整数 作为容量 capacity 初始化 LRU 缓存
    int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
    void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value ；
    如果不存在，则向缓存中插入该组 key-value 。
    如果插入操作导致关键字数量超过capacity，则应该逐出最久未使用的关键字。
    函数get和put必须以 O(1) 的平均时间复杂度运行。
     */

    private int capacity;
    private Map<Integer, Node> keyToNode;
    Node dummy = new Node(0, 0);
    private static class Node {
        Node pre, next;
        int key, value;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public Problem_146_LRUCache(int capacity) {
        this.capacity = capacity;
        this.keyToNode = new HashMap<Integer, Node>(capacity);
        this.dummy.next = dummy;
        this.dummy.pre = dummy;
    }

    public int get(int key) {
        Node node = getNode(key);
        return node == null ? -1 : node.value;
    }

    public void put(int key, int value) {
        Node node = getNode(key);
        if(node != null) {
            node.value = value;
            return;
        }
        node = new Node(key, value); // 新书
        keyToNode.put(key, node);
        addToFirst(node); // 放在最上面
        if (keyToNode.size() > capacity) { // 书太多了
            Node backNode = dummy.pre;
            keyToNode.remove(backNode.key);
            removeNode(backNode); // 去掉最后一本书
        }
    }

    private Node getNode(Integer key) {
        if (keyToNode.containsKey(key)) {
            Node node = keyToNode.get(key);
            removeNode(node);
            addToFirst(node);
            return node;
        }
        return null;
    }

    private void removeNode(Node node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }
    private void addToFirst(Node node) {
        node.pre = dummy;
        node.next = dummy.next;
        dummy.next.pre = node;
        dummy.next = node;
    }

    public static void main(String[] args) {
        Problem_146_LRUCache lruCache = new Problem_146_LRUCache(2);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        System.out.println(lruCache.get(1));
        lruCache.put(3, 3);
        System.out.println(lruCache.get(2));
        lruCache.put(4, 4);
        System.out.println(lruCache.get(1));
        System.out.println(lruCache.get(3));
        System.out.println(lruCache.get(4));
    }
}
