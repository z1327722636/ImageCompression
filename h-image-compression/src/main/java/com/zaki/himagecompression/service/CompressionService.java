package com.zaki.himagecompression.service;

import com.zaki.himagecompression.compression.CompressionAlgorithm;
import com.zaki.himagecompression.compression.DCTCompression;
import com.zaki.himagecompression.compression.HuffmanCompression;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CompressionService {

    private final Map<String, CompressionAlgorithm> algorithms = new HashMap<>();

    public CompressionService() {
        algorithms.put("Huffman", new HuffmanCompression());
        algorithms.put("DCT", new DCTCompression());

    }
    public CompressionAlgorithm getAlgorithm(String name) {
        return algorithms.getOrDefault(name, new HuffmanCompression());
    }
}
