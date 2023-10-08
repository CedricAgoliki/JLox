package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, Arrays.asList(
                "Binary: Expr left, Token operator, Expr right",
                "Grouping: Expr expression",
                "Literal: Object value",
                "Unary: Token operator, Expr right"
        ));
    }

    private static void defineAst(String outputDir, List<String> types) throws IOException {
        String path = outputDir + "/Expr.java";
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
        writer.println("package lox;\n");
        writer.println("// This file is generated by tool/GenerateAst.java\n");
        writer.println("import java.util.List;\n");
        writer.println("abstract class Expr {");
        for (String type: types) {
            String[] parts = type.split(":");
            String className = parts[0].trim();
            String fields = parts[1].trim();
            defineTypes(writer, className, fields);
        }
        writer.println("}");
        writer.close();

    }

    private static void defineTypes(PrintWriter writer, String className, String fields) {
        writer.println("    static class " + className + " extends Expr {");
        String[] fieldList = fields.trim().split(",");

        //fields
        for (String field: fieldList) {
            String[] parts = field.trim().split(" ");
            String fieldType = parts[0].trim();
            String fieldName = parts[1].trim();
            writer.println("        final " + fieldType + " " + fieldName + ";");
        }

        // constructor
        writer.print("      " + className + "(");
        for (int i = 0; i < fieldList.length; i++) {
            String[] parts = fieldList[i].trim().split(" ");
            String fieldType = parts[0].trim();
            String fieldName = parts[1].trim();
            if (i != 0) writer.print(", ");
            writer.print(fieldType + " " + fieldName);
        }
        writer.println(") {");
        for (String field: fieldList) {
            String[] parts = field.trim().split(" ");
            String fieldName = parts[1].trim();
            writer.println("            this." + fieldName + " = " + fieldName + ";");
        }
        writer.println("        }");
        writer.println("    }\n");
    }
}
