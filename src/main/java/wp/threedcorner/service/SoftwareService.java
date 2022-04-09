package wp.threedcorner.service;

import org.springframework.web.multipart.MultipartFile;
import wp.threedcorner.model.Software;

import java.util.List;

public interface SoftwareService {
    public List<Software> findAll();
    public Software findById(Long id);
    public Software createSoftware(MultipartFile logo, String name);
    public Software editSoftware(Long softwareId, MultipartFile logo, String name);
    public void deleteSoftware(Long softwareId);
}
