package wp.threedcorner.service;

import org.springframework.web.multipart.MultipartFile;

public interface SoftwareService {
    public void createSoftware(String username, MultipartFile logo, String name);
    public void editSoftware(String username, Long softwareId, MultipartFile logo, String name);
    public void deleteSoftware(String username, Long softwareId);
}
