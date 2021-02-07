package org.tosi29;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        try {
            JavaSourceList list = new JavaSourceList(Paths.get("./target_code/"));
            System.out.println("## List of Target Code ");
            System.out.println(list.toString());

            System.out.println("## List of Endpoint ");
            ParserHandler handler = new ParserHandler(list);
            List<EndPoint> endpoints = handler.executeVisitor();

            for (EndPoint endpoint: endpoints) {
                System.out.println(endpoint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
