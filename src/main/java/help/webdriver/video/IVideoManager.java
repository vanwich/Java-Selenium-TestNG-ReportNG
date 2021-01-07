package help.webdriver.video;

import java.io.File;
import java.util.Optional;

public interface IVideoManager {
    public Optional<File> get(String sid);
    public Optional<Boolean> delete(String sid);
}
