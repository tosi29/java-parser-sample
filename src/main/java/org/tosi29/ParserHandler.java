package org.tosi29;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ParserHandler {
    private List<CompilationUnit> compilationUnits;

    public ParserHandler(JavaSourceList javaSourceList) {
        List<Path> list = javaSourceList.getPathList();
        compilationUnits = list.stream()
                .map(x -> parse(x))
                .map(x -> x.getResult().get()) // FIXME optional handler
                .collect(Collectors.toList());
    }

    private ParseResult<CompilationUnit> parse(Path path) {
        try {
            return new JavaParser().parse(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;    // FIXME exception handling
        }
    }

    public void executeVisitor() {
        for (CompilationUnit unit: compilationUnits) {
            unit.accept(new EndpointNameVisitor(), "args");
        }
    }
}
