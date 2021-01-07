package help.webdriver.video;

import java.io.File;
import java.util.Optional;

public class NullVideoManager implements IVideoManager{
    @Override
    public Optional<File> get(String t) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> delete(String sid) {
        return Optional.empty();
    }
}
