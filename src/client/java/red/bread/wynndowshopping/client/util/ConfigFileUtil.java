package red.bread.wynndowshopping.client.util;

import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;

public class ConfigFileUtil {

    public static void writeFile(String fileName, String content) {
        File configDirectory = new File(MinecraftClient.getInstance().runDirectory, "config");

        if (!configDirectory.exists()) {
            if (!configDirectory.mkdirs()) {
                return;
            }
        }

        File fileToWrite = new File(configDirectory, fileName);

        try (FileWriter writer = new FileWriter(fileToWrite)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String fileName) {
        File configDirectory = new File(MinecraftClient.getInstance().runDirectory, "config");

        File fileToRead = new File(configDirectory, fileName);

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileToRead))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
