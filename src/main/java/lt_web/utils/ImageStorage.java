package lt_web.utils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import jakarta.servlet.http.Part;

public final class ImageStorage {
    private ImageStorage() { }
    public static Path directory() throws IOException {
        String configured = System.getProperty("app.upload.dir", System.getenv("UPLOAD_DIR"));
        Path path = configured == null || configured.isBlank()
                ? Path.of(System.getProperty("catalina.base", "."), "uploads", "category") : Path.of(configured);
        return Files.createDirectories(path.toAbsolutePath().normalize());
    }
    public static String save(Part part) throws IOException {
        if (part == null || part.getSize() == 0) return null;
        if (part.getSize() > 5 * 1024 * 1024) throw new IllegalArgumentException("Ảnh tối đa 5 MB.");
        // Read dimensions before decoding to reject oversized compressed images.
        try (InputStream input = part.getInputStream(); ImageInputStream stream = ImageIO.createImageInputStream(input)) {
            var readers = ImageIO.getImageReaders(stream);
            if (!readers.hasNext()) throw new IllegalArgumentException("Chỉ chấp nhận ảnh PNG hoặc JPEG hợp lệ.");
            var reader = readers.next();
            try {
                String format = reader.getFormatName();
                if (!format.equalsIgnoreCase("png") && !format.equalsIgnoreCase("jpeg"))
                    throw new IllegalArgumentException("Chỉ chấp nhận ảnh PNG hoặc JPEG.");
                reader.setInput(stream);
                if (reader.getWidth(0) > 4096 || reader.getHeight(0) > 4096)
                    throw new IllegalArgumentException("Kích thước ảnh tối đa 4096 × 4096 pixel.");
                BufferedImage image = reader.read(0);
                String filename = UUID.randomUUID() + ".png";
                ImageIO.write(image, "png", directory().resolve(filename).toFile());
                return filename;
            } finally { reader.dispose(); }
        } catch (javax.imageio.IIOException e) {
            throw new IllegalArgumentException("Nội dung ảnh không hợp lệ hoặc bị hỏng.");
        }
    }
    public static void delete(String filename) throws IOException {
        if (filename != null && filename.matches("[a-f0-9-]{36}\\.png")) Files.deleteIfExists(directory().resolve(filename));
    }
}
