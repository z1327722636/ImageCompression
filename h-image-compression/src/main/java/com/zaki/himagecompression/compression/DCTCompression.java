package com.zaki.himagecompression.compression;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DCTCompression implements CompressionAlgorithm {

    @Override
    public Map<String, Object> compress(String inputFilePath, String outputFilePath) throws Exception {
        System.out.println("DCT compression applied on: " + inputFilePath);

        // 确保输入文件存在并可读
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("Input file does not exist or cannot be read: " + inputFilePath);
        }

        // 读取图像
        BufferedImage image = ImageIO.read(inputFile);

        // 获取图像写入器
        ImageWriter writer = ImageIO.getImageWritersBySuffix(getFileExtension(outputFilePath)).next();

        // 获取写入参数
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);  // 设置压缩质量
        }

        // 输出压缩图像
        try (FileOutputStream fos = new FileOutputStream(outputFilePath);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
        }
        writer.dispose();

        // 获取原始文件和压缩文件大小
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int originalDataSize = imageWidth * imageHeight;
        int encodedDataSize = (int)Files.size(Paths.get(outputFilePath));
        double compressionRatio = (double) encodedDataSize / originalDataSize ;
        String compressionRatioPercentage = String.format("%.2f%%", compressionRatio * 100);
        System.out.println("Compression Ratio: " + compressionRatioPercentage);
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("originalDataSize", originalDataSize);
        result.put("encodedDataSize", encodedDataSize);
        result.put("compressionRatio", compressionRatioPercentage);
        return result;
    }

    private String getFileExtension(String filePath) {
        String fileName = new File(filePath).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
