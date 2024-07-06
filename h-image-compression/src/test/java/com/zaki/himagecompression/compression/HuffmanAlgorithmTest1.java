package com.zaki.himagecompression.compression;

import org.junit.Test;

import java.util.Map;
import java.util.*;

public class HuffmanAlgorithmTest1 {

    @Test
    public void testHuffmanEncoding() {
        String input = "abcdefg";
        Map<Character, String> huffmanCodes = generateHuffmanCodes(input);

        // 打印霍夫曼编码
        System.out.println("Character\tHuffman Code");
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }}

    private Map<Character, String> generateHuffmanCodes(String input) {
        char[] chars = input.toCharArray();
        Map<Character, Integer> characterMap = new HashMap<>();

        // 计算每个字符的频率
        for (int i = 0; i < chars.length; i++) {
            int count = characterMap.containsKey(chars[i]) ? characterMap.get(chars[i]) : 0;
            characterMap.put(chars[i], count + 1);
        }

        // 构建优先队列
        PriorityQueue<HFNode> pQueue = new PriorityQueue<>();
        Set<Character> key = characterMap.keySet();
        for (char c : key) {
            HFNode hfNode = new HFNode();
            hfNode.setCharacter(c);
            hfNode.setFrequence(characterMap.get(c));
            pQueue.add(hfNode);
        }

        // 构建霍夫曼树
        while (pQueue.size() > 1) {
            HFNode newNode = new HFNode();
            HFNode leftNode = pQueue.remove();
            HFNode rightNode = pQueue.remove();
            newNode.setFrequence(rightNode.getFrequence() + leftNode.getFrequence());
            newNode.setLeft(leftNode);
            newNode.setRight(rightNode);
            newNode.setCode("");
            newNode.setCharacter(newNode.getLeft().getCharacter());
            pQueue.add(newNode);
        }

        HFNode finalTree = pQueue.remove();
        Queue<HFNode> queue = new ArrayDeque<>();
        if (finalTree != null) {
            queue.add(finalTree);
            if (finalTree.getLeft() != null) finalTree.getLeft().setCode("0");
            if (finalTree.getRight() != null) finalTree.getRight().setCode("1");
        }

        // 生成霍夫曼编码
        Map<Character, String> dict = new HashMap<>();

        while (!queue.isEmpty()) {
            HFNode node = queue.remove();
            if (node.getLeft() != null) node.getLeft().setCode(node.getCode() + "0");
            if (node.getRight() != null) node.getRight().setCode(node.getCode() + "1");

            if (node.getLeft() != null) {
                queue.add(node.getLeft());
            }

            if (node.getRight() != null) {
                queue.add(node.getRight());
            }

            if (node.getLeft() == null && node.getRight() == null) {
                dict.put(node.character, node.getCode());
            }
        }

        return dict;
    }

    // 霍夫曼树节点类
    private static class HFNode implements Comparable<HFNode> {
        char character;
        int frequence;
        private HFNode left;
        private HFNode right;
        private String code;

        public char getCharacter() {
            return character;
        }

        public void setCharacter(char character) {
            this.character = character;
        }

        public int getFrequence() {
            return frequence;
        }

        public void setFrequence(int frequence) {
            this.frequence = frequence;
        }

        public HFNode getLeft() {
            return left;
        }

        public void setLeft(HFNode left) {
            this.left = left;
        }

        public HFNode getRight() {
            return right;
        }

        public void setRight(HFNode right) {
            this.right = right;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return this.character + " " + this.frequence + " ";
        }

        @Override
        public int compareTo(HFNode o) {
            if (this.frequence == o.frequence) {
                return this.getCharacter() - o.getCharacter();
            }
            return this.frequence - o.frequence;
        }
    }
}
