package wp.threedcorner.model.dto;

import lombok.Data;

@Data
public class LikeProjectDto {
    private boolean isLikedNow;
    private int likes;

    public LikeProjectDto(boolean isLikedNow, int likes) {
        this.isLikedNow = isLikedNow;
        this.likes = likes;
    }

    public LikeProjectDto() {
    }
}
