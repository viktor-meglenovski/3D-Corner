package wp.threedcorner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import wp.threedcorner.config.Constants;
import wp.threedcorner.model.Image;
import wp.threedcorner.repository.ImageRepository;
import wp.threedcorner.service.ImageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image getDefaultUserImage() {
        return imageRepository.findByLocationContaining("default-user-image.png");
    }

    @Override
    public Image saveImage(MultipartFile image, String path, String location) {
        Image i=new Image();
        try {
            byte[] bytes = new byte[0];
            bytes = image.getBytes();
            Path p = Paths.get(path);
            Files.write(p, bytes);
            i=new Image(location);
            imageRepository.save(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Override
    public void deletePhysicalImage(String location) {
        File f=new File(Constants.rootPath+location);
        f.delete();
    }
}
