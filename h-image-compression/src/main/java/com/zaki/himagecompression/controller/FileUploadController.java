package com.zaki.himagecompression.controller;

import com.zaki.himagecompression.service.CompressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads/";
    private static final String COMPRESSION_DIR = "compressionimage/";

    @Autowired
    private CompressionService compressionService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload file: " + e.getMessage());
        }
    }

    // 使用@PostMapping注解定义了一个处理POST请求的方法，URL路径为"/compress"。
    @PostMapping("/compress")
    public ResponseEntity<Map<String, Object>> compressFiles(@RequestBody Map<String, Object> request) {

        // 从请求体中获取压缩算法和文件列表。
        String compressionAlgorithm = (String) request.get("compressionAlgorithm");
        List<String> files = (List<String>) request.get("files");

        // 检查文件列表是否为空，如果为空则返回错误响应。
        if (files == null || files.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No files to compress");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 尝试执行文件压缩的逻辑
        try {
            // 使用Java 8的流操作遍历文件列表，对每个文件进行压缩处理。
            List<Map<String, Object>> compressedFiles = files.stream().map(file -> {
                try {
                    // 生成压缩文件的名称，例如将"example.txt"变成"example_compressed.txt"。
                    //String compressedFileName = file.replace(".", "_compressed.hfmc");
                    // 提取文件名和后缀名
                    int dotIndex = file.lastIndexOf(".");
                    String nameWithoutExtension = file.substring(0, dotIndex);
                    String extension = file.substring(dotIndex);

//                    String compressedFileName = nameWithoutExtension + "_compressed.hfmc";
                    String compressedFileName;

                    switch (compressionAlgorithm) {
                        case "Huffman":
                            compressedFileName = nameWithoutExtension + "_compressed.hfmc";
                            break;
                        case "DCT":
                            compressedFileName = nameWithoutExtension + "_compressed" + extension;
                            break;
                        default:
                            compressedFileName = nameWithoutExtension + "_compressed.unknown";
                            break;
                    }
                    // 定义输入和输出文件的路径。
                    String inputFilePath = UPLOAD_DIR + file;
                    String outputFilePath = COMPRESSION_DIR + compressedFileName ;
                    // 使用compressionService中的对应算法进行文件压缩。
                    Map<String, Object> result = compressionService.getAlgorithm(compressionAlgorithm).compress(inputFilePath, outputFilePath);

                    // 创建包含文件信息的映射，包括原始名称、压缩名称、大小、压缩率等。
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("originalName", file);
                    fileInfo.put("compressedName", compressedFileName);
                    fileInfo.put("originalSize",(int) result.get("originalDataSize") + " bytes"); // 获取原始文件数据大小
                    fileInfo.put("compressedSize",(int) result.get("encodedDataSize") + " bytes"); // 获取压缩后编码数据的总字节数
                    // 计算压缩率（节约的百分比）。
                    fileInfo.put("compressionRate",result.get("compressionRatio"));
                    // 添加缩略图的URL（这里假设有一个服务可以提供缩略图）。
                    fileInfo.put("thumbnail", "http://localhost:8081/api/download/" + compressedFileName);
                    fileInfo.put("algorithm", compressionAlgorithm); // 记录使用的压缩算法
                    return fileInfo; // 返回包含文件信息的映射。
                } catch (Exception e) {
                    e.printStackTrace(); // 打印异常信息
                    return null; // 发生异常时返回null（此处可能应该抛出异常或者返回错误信息）
                }
            }).collect(Collectors.toList()); // 收集所有映射到一个列表中。

            // 创建响应映射，包含成功信息、使用的压缩算法和压缩后的文件列表。
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Files compressed successfully");
            response.put("compressedFiles", compressedFiles); // 存储压缩后的文件列表。
            // 返回包含成功信息的响应。
            return ResponseEntity.ok(response); // 状态码为200 OK。

        } catch (Exception e) { // 捕获处理文件压缩过程中的异常。
            e.printStackTrace(); // 打印异常信息到控制台。
            // 创建包含错误信息的响应映射。
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Compression failed: " + e.getMessage()); // 设置错误消息。
            // 返回包含错误信息的响应，状态码为500 Internal Server Error。
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

}
    // 私有方法，计算文件的压缩率。
    private String calculateCompressionRate(long originalSize, long compressedSize) {
        if (originalSize == 0) return "N/A";
        double rate = ((double) (originalSize - compressedSize) / originalSize) * 100;
        return String.format("%.2f%%", rate);
    }
}
