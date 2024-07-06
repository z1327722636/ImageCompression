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

public class HuffmanDecompressionTest {

    private static final String INPUT_FILE_PATH = "src/test/resources/3.bmp";
    private static final String COMPRESSED_FILE_PATH = "src/test/resources/3_compressed.hfmc";
    private static final String DECOMPRESSED_FILE_PATH = "src/test/resources/3_decompressed.bmp";


    @Test
    public void testHuffmanCompressionDecompression() throws Exception {
        HuffmanCompression huffmanCompression = new HuffmanCompression();

        // 解压缩文件
        huffmanCompression.decompress(COMPRESSED_FILE_PATH, DECOMPRESSED_FILE_PATH);

        // 验证解压缩后的文件存在
        File decompressedFile = new File(DECOMPRESSED_FILE_PATH);
        assertTrue(decompressedFile.exists());

        // 验证解压缩后的图像能够被正确读取
        BufferedImage decompressedImage = ImageIO.read(decompressedFile);
        assertNotNull(decompressedImage);

        // 验证解压缩后的图像尺寸与原始图像一致
        BufferedImage originalImage = ImageIO.read(new File(INPUT_FILE_PATH));
        assertNotNull(originalImage);
        assertTrue(originalImage.getWidth() == decompressedImage.getWidth());
        assertTrue(originalImage.getHeight() == decompressedImage.getHeight());


    }
}
