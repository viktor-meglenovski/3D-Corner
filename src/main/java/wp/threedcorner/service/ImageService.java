package wp.threedcorner.service;

import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.Image;

import java.util.Optional;

public interface ImageService {
    public Image getDefaultUserImage();
    public Image saveImage(MultipartFile image, String path, String location);
    public void deletePhysicalImage(String location);
    public void deleteImageFromDatabase(Image image);
}
