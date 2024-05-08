package com.danieldigiovanni;

import com.danieldigiovanni.expr.Expr;
import com.danieldigiovanni.expr.visitor.AstTreePrinterVisitor;
import com.danieldigiovanni.interpreter.Interpreter;
import com.danieldigiovanni.interpreter.exception.LoxRuntimeException;
import com.danieldigiovanni.lexer.Lexer;
import com.danieldigiovanni.parser.Parser;
import com.danieldigiovanni.parser.exception.LoxParseException;
import com.danieldigiovanni.token.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The Lox interpreter.
 */
public class Lox {

    /**
     * Error property to be set to true when there is an error in the Lox code.
     */
    private static boolean error;

    /**
     * Reports an error by printing it to stderr.
     *
     * @param line The line number that the error occurred on.
     * @param message The error message.
     */
    private static void reportCompilationError(int line, String message) {
        System.err.println(String.format(
            "[line %d] Compilation Error: %s",
            line,
            message
        ));
        error = true;
    }

    /**
     * Reports an error by printing it to stderr.
     *
     * @param line The line number that the error occurred on.
     * @param message The error message.
     */
    private static void reportRuntimeError(int line, String message) {
        System.err.println(String.format(
            "[line %d] Runtime Error: %s",
            line,
            message
        ));
        error = true;
    }

    /**
     * Report an error.
     *
     * @param line The line number that the error occurred on.
     * @param message The error message.
     */
    private static void error(int line, String message) {
        reportCompilationError(line, message);
    }

    private static void error(LoxParseException parseException) {
        reportCompilationError(
            parseException.getToken().getLineNumber(),
            parseException.getMessage()
        );
    }

    private static void error(LoxRuntimeException interpreterException) {
        reportRuntimeError(
            interpreterException.getToken().getLineNumber(),
            interpreterException.getMessage()
        );
    }

    /**
     * Run the Lox interpreter on a string of Lox source code.
     *
     * @param source Some Lox source code.
     */
    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.lexAllTokens();
        Parser parser = new Parser(tokens);

        try {
            Expr expression = parser.parse();
            if (expression != null) {
//                System.out.println(new AstPrinterVisitor().print(expression));
                System.out.println(new AstTreePrinterVisitor().print(expression));
                Interpreter interpreter = new Interpreter();
                interpreter.interpret(expression);
            } else {
                System.out.println("ERROR");
                for (Token token : tokens) {
                    System.out.println(token);
                }
            }
        } catch (LoxParseException e) {
            error(e);
        } catch (LoxRuntimeException e) {
            error(e);
        }
    }

    /**
     * Run the Lox interpreter on a file containing Lox source code.
     *
     * @param path The path of the file.
     *
     * @throws IOException If there is an error reading the file.
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (error) {
            System.exit(65);
        }
    }

    /**
     * Interactive Lox interpreter in the console.
     *
     * @throws IOException If there is an error reading user input.
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.println("Lox > ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            error = false;
        }
    }

    /**
     * The main entry point for the Lox interpreter.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

}
