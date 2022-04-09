package wp.threedcorner.config;

import org.springframework.stereotype.Component;
import wp.threedcorner.model.Image;
import wp.threedcorner.service.ImageService;

@Component
public class Constants {
    public static final String userRootPath ="src//main//resources//static//user_uploads//";
    public static final String adminRootPath ="src//main//resources//static//admin_uploads//";
    public static final String rootPath="src//main//resources/static";
}
