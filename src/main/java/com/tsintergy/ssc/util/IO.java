package com.tsintergy.ssc.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class IO {
    /**
     * 获取资源文件的输入流
     *
     * @param resources 资源文件
     * @return 文件输入流
     */
    public static InputStream getInputStream(String resources) {
        return IO.class.getClassLoader().getResourceAsStream(resources);
    }

    /**
     * 读取插件内部 Classpath 资源文件内容信息
     *
     * @param resources 资源路径
     * @return 文件内容
     */
    public static String readResources(String resources) {
        return read(getInputStream(resources));
    }

    /**
     * 从输入流中读取字符串内容
     *
     * @param inputStream 输入流
     * @return 字符串内容
     */
    public static String read(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                builder.append(reader.readLine());
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inputStream);
        }
        return builder.toString();
    }

    /**
     * 关闭输入、输出流
     *
     * @param closeables 可关闭流对象
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
