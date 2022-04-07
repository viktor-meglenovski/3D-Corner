package wp.threedcorner.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.config.Constants;
import wp.threedcorner.model.Image;
import wp.threedcorner.model.Software;
import wp.threedcorner.model.exceptions.NoAuthorityException;
import wp.threedcorner.repository.ImageRepository;
import wp.threedcorner.repository.SoftwareRepository;
import wp.threedcorner.service.SoftwareService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@Service
public class SoftwareServiceImpl implements SoftwareService {
    private final SoftwareRepository softwareRepository;
    private final ImageRepository imageRepository;
    public SoftwareServiceImpl(SoftwareRepository softwareRepository, ImageRepository imageRepository) {
        this.softwareRepository = softwareRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public void createSoftware(String username, MultipartFile logo, String name) {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if(authorities.contains("ROLE_ADMIN")){
            Software s=new Software();
            s.setName(name);

            //save logo
            try {
                byte[] bytes = new byte[0];
                bytes = logo.getBytes();
                Path path = Paths.get(Constants.adminRootPath +logo.getOriginalFilename());
                Files.write(path, bytes);
                Image image=new Image(path.toString());
                imageRepository.save(image);
                s.setLogo(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            softwareRepository.save(s);
        }
        else throw new NoAuthorityException();
    }

    @Override
    public void editSoftware(String username, Long softwareId, MultipartFile logo, String name) {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if(authorities.contains("ROLE_ADMIN")){
            Software s=softwareRepository.getById(softwareId);
            s.setName(name);
            if(logo!=null){
                //dali fajlot od staroto logo ke se brisi?
                try {
                    byte[] bytes = new byte[0];
                    bytes = logo.getBytes();
                    Path path = Paths.get(Constants.adminRootPath +logo.getOriginalFilename());
                    Files.write(path, bytes);
                    Image image=new Image(path.toString());
                    imageRepository.save(image);
                    s.setLogo(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            softwareRepository.save(s);
        }
        else throw new NoAuthorityException();
    }

    @Override
    public void deleteSoftware(String username, Long softwareId) {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if(authorities.contains("ROLE_ADMIN"))
            softwareRepository.deleteById(softwareId);
        else throw new NoAuthorityException();
    }
}
