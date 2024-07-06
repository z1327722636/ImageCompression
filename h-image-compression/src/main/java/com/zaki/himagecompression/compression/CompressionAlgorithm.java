package com.zaki.himagecompression.compression;

import java.util.Map;

public interface CompressionAlgorithm {
    Map<String, Object> compress(String inputFilePath, String outputFilePath) throws Exception;
}
