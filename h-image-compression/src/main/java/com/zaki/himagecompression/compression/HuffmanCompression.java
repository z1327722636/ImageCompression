package com.zaki.himagecompression.compression;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import javax.imageio.ImageIO;

public class HuffmanCompression implements CompressionAlgorithm {

    private HFNode root;

    @Override
    public Map<String, Object> compress(String inputFilePath, String outputFilePath) throws Exception {
        System.out.println("Huffman compression applied on: " + inputFilePath);

        // 确保输入文件存在并可读
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read: " + inputFilePath);
        }

        // 将图像转换为字符序列
        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IllegalArgumentException("Failed to read image from input file: " + inputFilePath);
        }

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int originalDataSize = imageWidth * imageHeight;
        StringBuilder charSequence = new StringBuilder();
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int rgb = image.getRGB(x, y);
                int grayValue = (rgb & 0xFF); // 获取灰度值
                charSequence.append((char) grayValue); // 将灰度值转换为字符
            }
        }

        // 生成霍夫曼编码
        Map<Character, String> huffmanCodes = generateHuffmanCodes(charSequence.toString());

        // 对字符序列进行编码
        StringBuilder encodedString = new StringBuilder();
        for (char c : charSequence.toString().toCharArray()) {
            encodedString.append(huffmanCodes.get(c));
        }

// 计算编码后图像数据总位数
        int dataBitCount = encodedString.length();

// 生成权重映射表
        Map<Character, Integer> characterMap = new HashMap<>();
        for (char c : charSequence.toString().toCharArray()) {
            characterMap.put(c, characterMap.getOrDefault(c, 0) + 1);
        }
        int weightMapValCount = characterMap.size();

// 将数据写入文件
        int encodedDataSize = 0;

        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            // 写入原图像文件头（54字节）
            byte[] header = new byte[54];
            fos.write(header);

            // 写入编码后图像数据总位数（4字节）
            ByteBuffer bb = ByteBuffer.allocate(4).putInt(dataBitCount);
            fos.write(bb.array());

            // 写入原图像文件中不同的数据单元的数目（4字节）
            bb.clear();
            bb.putInt(weightMapValCount);
            fos.write(bb.array());

            // 写入权重映射表
            for (Map.Entry<Character, Integer> entry : characterMap.entrySet()) {
                fos.write((byte) entry.getKey().charValue());
                bb.clear();
                bb.putInt(entry.getValue());
                fos.write(bb.array());
            }

            // 写入编码数据
            int remainingBits = 8 - (dataBitCount % 8);
            if (remainingBits == 8) remainingBits = 0;
            for (int i = 0; i < dataBitCount; i += 8) {
                int endIndex = Math.min(i + 8, dataBitCount);
                String byteString = encodedString.substring(i, endIndex);
                if (endIndex - i < 8) {
                    StringBuilder padding = new StringBuilder();
                    for (int j = 0; j < 8 - (endIndex - i); j++) {
                        padding.append("0");
                    }
                    byteString += padding.toString();
                }
                fos.write((byte) Integer.parseInt(byteString, 2));
            }
            encodedDataSize = (dataBitCount + remainingBits) / 8;
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 计算压缩比
        double compressionRatio = (double) encodedDataSize / originalDataSize;
        String compressionRatioPercentage = String.format("%.2f%%", compressionRatio * 100);
        System.out.println("Compression Ratio: " + compressionRatioPercentage);

        System.out.println("originalDataSize size: " + originalDataSize + " bytes");
        System.out.println("总位数: " + dataBitCount + " bytes");

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("originalDataSize", originalDataSize);
        result.put("encodedDataSize", encodedDataSize);
        result.put("compressionRatio", compressionRatioPercentage);

        return result;
    }

    public void decompress(String inputFilePath, String outputFilePath) throws Exception {
        System.out.println("Huffman decompression applied on: " + inputFilePath);

        // 确保输入文件存在并可读
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read: " + inputFilePath);
        }

        // 读取编码后的数据
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] header = new byte[54];
            fis.read(header);

            byte[] intBuffer = new byte[4];

            // 读取编码后图像数据总位数
            fis.read(intBuffer);
            int dataBitCount = ByteBuffer.wrap(intBuffer).getInt();

            // 读取原图像文件中不同的数据单元的数目
            fis.read(intBuffer);
            int weightMapValCount = ByteBuffer.wrap(intBuffer).getInt();

            // 读取权重映射表
            Map<Character, Integer> characterMap = new HashMap<>();
            for (int i = 0; i < weightMapValCount; i++) {
                char character = (char) fis.read();
                fis.read(intBuffer);
                int frequency = ByteBuffer.wrap(intBuffer).getInt();
                characterMap.put(character, frequency);
            }

            // 重新构建霍夫曼树
            PriorityQueue<HFNode> pQueue = new PriorityQueue<>();
            for (Map.Entry<Character, Integer> entry : characterMap.entrySet()) {
                HFNode hfNode = new HFNode();
                hfNode.setCharacter(entry.getKey());
                hfNode.setFrequence(entry.getValue());
                pQueue.add(hfNode);
            }
            while (pQueue.size() > 1) {
                HFNode newNode = new HFNode();
                HFNode leftNode = pQueue.remove();
                HFNode rightNode = pQueue.remove();
                newNode.setFrequence(rightNode.getFrequence() + leftNode.getFrequence());
                newNode.setLeft(leftNode);
                newNode.setRight(rightNode);
                pQueue.add(newNode);
            }
            this.root = pQueue.poll();

            // 读取编码数据
            StringBuilder encodedString = new StringBuilder();
            int byteRead;
            while ((byteRead = fis.read()) != -1) {
                String byteString = String.format("%8s", Integer.toBinaryString(byteRead & 0xFF)).replace(' ', '0');
                encodedString.append(byteString);
            }

            // 解码霍夫曼编码
            StringBuilder decodedString = new StringBuilder();
            HFNode node = root;
            for (char bit : encodedString.toString().toCharArray()) {
                node = (bit == '0') ? node.left : node.right;
                if (node.left == null && node.right == null) {
                    decodedString.append(node.character);
                    node = root;
                }
            }

            // 将解码后的数据重新生成图像
            int imageWidth = 256;  // 替换为原始图像的宽度
            int imageHeight = 256; // 替换为原始图像的高度
            BufferedImage decompressedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);
            int index = 0;
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    if (index < decodedString.length()) {
                        int grayValue = decodedString.charAt(index++);
                        int rgb = (grayValue << 16) | (grayValue << 8) | grayValue;
                        decompressedImage.setRGB(x, y, rgb);
                    }
                }
            }

            // 获取原图像的格式
//            String formatName = getFileExtension(outputFilePath);

            // 保存解压缩后的图像
            File outputFile = new File(outputFilePath);
            ImageIO.write(decompressedImage, ".bmp", outputFile);
        }
    }

    // 生成霍夫曼编码
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
