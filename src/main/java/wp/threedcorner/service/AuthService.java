package wp.threedcorner.service;

import wp.threedcorner.model.User;

public interface AuthService {
    User login(String username, String password);
}
