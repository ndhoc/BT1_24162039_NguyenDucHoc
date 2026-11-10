package lt_web.controllers;

import java.io.IOException;
import java.nio.file.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lt_web.utils.ImageStorage;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("fname");
        if (name == null || !name.matches("[a-f0-9-]{36}\\.png")) { resp.sendError(404); return; }
        Path file = ImageStorage.directory().resolve(name);
        if (!Files.isRegularFile(file)) { resp.sendError(404); return; }
        resp.setContentType("image/png"); resp.setContentLengthLong(Files.size(file));
        Files.copy(file, resp.getOutputStream());
    }
}
