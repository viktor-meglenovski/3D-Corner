package wp.threedcorner.model;

import lombok.Data;

@Data
public class TempProfilePicture {
    private String location;

    public TempProfilePicture(String location) {
        this.location = location;
    }
}
