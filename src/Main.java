import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Path path = Files.createDirectories(Paths.get("tst"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path timeFile = Paths.get("tst/tmp.txt");
        Path mainFile = Paths.get("tst/main.txt");

        try (FileChannel writeFromScannerChannel = FileChannel.open(timeFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE))//при атрибуте DELETE_ON_CLOSE файл не создается
        {
            ByteBuffer buf = ByteBuffer.allocate(256);
            Scanner sc = new Scanner(System.in);
            String s = new String();
            System.out.println("Tap symbols for writing in file, for stop tap /");
            while ((!sc.hasNext("/"))) {
                s = sc.next();
                buf.put(s.getBytes(StandardCharsets.UTF_8));
                buf.put("\n".getBytes());
            }
            buf.flip();
            writeFromScannerChannel.write(buf);

        } catch (IOException e) {
            e.printStackTrace();
        }
//////////reading and rewrite to main
        try (FileChannel writeToMainChannel = FileChannel.open(mainFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            try (
                    BufferedReader reader = Files.newBufferedReader(timeFile);
            ) {
                String line;
                ByteBuffer buf2 = ByteBuffer.allocate(256);
                while ((line = reader.readLine()) != null) {
                    buf2.put(line.getBytes(StandardCharsets.UTF_8));
                    buf2.put("\n".getBytes());
                }
                String date = String.valueOf(LocalDateTime.now());
                buf2.put(date.getBytes(StandardCharsets.UTF_8));
                buf2.put("\n".getBytes(StandardCharsets.UTF_8));
                buf2.flip();
                writeToMainChannel.write(buf2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter writer = Files.newBufferedWriter(timeFile);//очистка временного файла
        writer.write("");
        writer.flush();
    }
}
