package pl.edu.agh.po;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utilities
{
    private static Utilities instance;


    public static Utilities getInstance(){
        if (instance == null){
            instance = new Utilities();
        }
        return instance;
    }

    public void backupDatabase() {
        Path source = Paths.get("rp.db");
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        //dynamiczne xd xd
        Path backup = Paths.get("G:/Mój dysk/backup/rp" + timestamp + ".db.bak");
        try {
            Files.copy(
                    source,
                    backup,
                    StandardCopyOption.REPLACE_EXISTING
            );
            System.out.println("Backup wykonany");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
