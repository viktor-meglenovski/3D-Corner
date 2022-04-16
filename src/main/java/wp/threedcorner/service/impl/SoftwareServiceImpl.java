package wp.threedcorner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.config.Constants;
import wp.threedcorner.model.Image;
import wp.threedcorner.model.Software;
import wp.threedcorner.model.exceptions.SoftwareDoesNotExistException;
import wp.threedcorner.repository.SoftwareRepository;
import wp.threedcorner.service.ImageService;
import wp.threedcorner.service.ProjectService;
import wp.threedcorner.service.SoftwareService;

import java.util.List;

@Service
public class SoftwareServiceImpl implements SoftwareService {
    private final SoftwareRepository softwareRepository;
    private final ImageService imageService;
    private final ProjectService projectService;

    public SoftwareServiceImpl(SoftwareRepository softwareRepository, ImageService imageService, ProjectService projectService) {
        this.softwareRepository = softwareRepository;
        this.imageService = imageService;
        this.projectService = projectService;
    }

    @Override
    public List<Software> findAll() {
        return softwareRepository.findAll();
    }

    @Override
    public Software findById(Long id) {
        return softwareRepository.findById(id).orElseThrow(()->new SoftwareDoesNotExistException());
    }

    @Override
    public Software createSoftware(MultipartFile logo, String name) {
        Software s=new Software();
        s.setName(name);

        Image image=imageService.saveImage(logo,
                Constants.adminRootPath+"software/" +logo.getOriginalFilename(),
                "/admin_uploads/software/"+logo.getOriginalFilename());
        s.setLogo(image);

        softwareRepository.save(s);
        return s;
    }

    @Override
    public Software editSoftware(Long softwareId, MultipartFile logo, String name) {
        Software s=softwareRepository.getById(softwareId);
        s.setName(name);
        if(!logo.isEmpty()){
            imageService.deletePhysicalImage(s.getLogo().getLocation());
            Image image=imageService.saveImage(logo,
                    Constants.adminRootPath+"software/" +logo.getOriginalFilename(),
                    "/admin_uploads/software/"+logo.getOriginalFilename());
            s.setLogo(image);
        }
        softwareRepository.save(s);
        return s;
    }

    @Override
    public void deleteSoftware(Long softwareId) {
        Software s=softwareRepository.getById(softwareId);
        projectService.deleteSotwareFromProjects(s);
        imageService.deletePhysicalImage(s.getLogo().getLocation());
        softwareRepository.deleteById(softwareId);
    }
}
