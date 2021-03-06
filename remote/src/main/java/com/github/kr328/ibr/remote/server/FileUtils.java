package com.github.kr328.ibr.remote.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

class FileUtils {
    static String readLines(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
            builder.append(line);

        reader.close();

        return builder.toString();
    }

    static void writeLines(File file, String data) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);

        stream.write(data.getBytes());

        stream.close();
    }
}
