package com.zaki.himagecompression.compression;

import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HuffmanCompressionTest {

//    private static final String INPUT_FILE_PATH = "src/test/resources/3.bmp";
private static final String INPUT_FILE_PATH = "src/test/resources/dog2.jpeg";
    private static final String COMPRESSED_FILE_PATH = "src/test/resources/dog2_compressed.hfmc";

//    @Before
//    public void setUp() throws IOException {
//        // 生成渐变色测试图像
//        int width = 256;
//        int height = 256;
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//        Graphics2D g2d = image.createGraphics();
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                int grayValue = (x * 255) / (width - 1);
//                Color color = new Color(grayValue, grayValue, grayValue);
//                g2d.setColor(color);
//                g2d.fillRect(x, y, 1, 1);
//            }
//        }
//        g2d.dispose();
//
//        // 确保目录存在
//        Files.createDirectories(Paths.get("src/test/resources"));
//
//        // 写入测试图像文件
//        File testImageFile = new File(INPUT_FILE_PATH);
//        ImageIO.write(image, "bmp", testImageFile);
//    }

    @Test
    public void testHuffmanCompression() throws Exception {
        HuffmanCompression huffmanCompression = new HuffmanCompression();
        Map<String, Object> result = huffmanCompression.compress(INPUT_FILE_PATH, COMPRESSED_FILE_PATH);

        // 验证压缩后的文件存在
        File compressedFile = new File(COMPRESSED_FILE_PATH);
        assertTrue(compressedFile.exists());

        // 验证压缩后的文件大小和压缩比
        long originalFileSize = Files.size(Paths.get(INPUT_FILE_PATH));
        int compressedFileSize = (int) result.get("encodedDataSize");
        double compressionRatio = (double) result.get("compressionRatio");

        // 打印文件大小和压缩比
        System.out.println("Original file size: " + originalFileSize + " bytes");
        System.out.println("Compressed file size: " + compressedFileSize + " bytes");
        System.out.println("Compression ratio: " + compressionRatio);

        // 断言压缩后的文件大小小于原文件大小
        assertTrue("Compressed file size (" + compressedFileSize + " bytes) is not smaller than original file size (" + originalFileSize + " bytes)",
                compressedFileSize < originalFileSize);

        // 验证压缩后的图像能够被正确读取
        BufferedImage compressedImage = ImageIO.read(compressedFile);
        assertNotNull(compressedImage);
    }
}
