package org.tosi29;

import java.io.IOException;
import java.nio.file.Paths;

public class App
{
    public static void main( String[] args )
    {
        try {
            JavaSourceList list = new JavaSourceList(Paths.get("./target_code/"));
            System.out.println(list.toString());

            ParserHandler handler = new ParserHandler(list);
            handler.executeVisitor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
