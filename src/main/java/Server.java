import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;

public class Server {
    public static void main(String[] args) {
        System.out.println("START");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                System.out.println("Accepting...");
                Socket socket = serverSocket.accept();
                new Thread(() -> Server.handler(socket)).start();
            }
        } catch (IOException exception) {// close socket
            exception.printStackTrace();
        }
    }// main

    public static void handler(Socket socket) {
        System.out.println("Accepted!");
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        ) {
            String str = null;

            while (!bufferedReader.ready()) {
                ;
            }

            // first line ( GET index.html HTTP ) into parts
            String firstLine = bufferedReader.readLine();
            System.out.println(firstLine);

            String[] parts = firstLine.split(" ");// if Chrome ask without index.html - all works bad

            System.out.println(firstLine);
            for (int i = 0; bufferedReader.ready(); ++i) {
                System.out.println(i + ": " + bufferedReader.readLine());
            }


            Path path = Paths.get(".", parts[1]);// index.html
//            Path path = Paths.get(".\\www", parts[1]);// index.html
//                    Path path = Paths.get("C:\\Users\\User\\IdeaProjects\\MyJava11\\www", parts[1]);// index.html
//                    Path path = Paths.get("\\MyJava11\\www\\dir", parts[1]);// index.html

            System.out.println(path.toString());

            if (!Files.exists(path)) {
                printWriter.println("HTTP/1.1 404 NOT_FOUND");
                printWriter.println("Content-Type: text/html; charset=utf-8");//version; answer code;
                printWriter.println();
                printWriter.println("<p> File not found </p>");
                printWriter.flush();
//                continue;// go to while()
            } else {
                printWriter.println("HTTP/1.1 200 OK");//version; answer code;
                printWriter.println("Content-Type: text/html; charset=utf-8");//version; answer code;
                printWriter.println(); // ???
                printWriter.flush();

                // java 11
                System.out.println(path.toString());
                BufferedReader trans = Files.newBufferedReader(path);
                trans.transferTo(printWriter);


//                    printWriter.println("<h1>" + "Hello!" + "</h>");
//                    printWriter.flush();
//                    System.out.println(counter);
            }
            System.out.println("END");
        } catch (IOException e) {// 8080
            System.out.println("error");
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
