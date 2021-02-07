package org.tosi29;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JavaSourceList {
    private List<Path> pathList;

    public JavaSourceList(Path root) throws IOException {
        pathList = Files.walk(root)
                .filter(x -> x.toString().toLowerCase().endsWith(".java"))
                .collect(Collectors.toList());
        Collections.unmodifiableList(pathList);
    }

    public List<Path> getPathList() {
        return pathList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Path path: pathList) {
            builder.append(path.toString())
                    .append("\n");
        }
        return builder.toString();
    }
}
