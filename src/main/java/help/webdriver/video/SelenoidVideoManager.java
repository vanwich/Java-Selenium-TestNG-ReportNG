package help.webdriver.video;

import java.io.File;
import java.util.Optional;
import help.webdriver.SelenoidHelper;

public class SelenoidVideoManager implements IVideoManager{
    private SelenoidHelper selenoidHelper;
    private String targetDir;
    private final String subPath = "";

    public SelenoidVideoManager(String targetDir) {
        selenoidHelper = new SelenoidHelper(videoName -> "/video/" + videoName);
        this.targetDir = targetDir;
    }

    @Override
    public Optional<File> get(String videoName) {
        return selenoidHelper.get(videoName, subPath, SelenoidHelper.buildFileGetter(videoName, targetDir));
    }

    @Override
    public Optional<Boolean> delete(String videoName) {
        return selenoidHelper.delete(videoName, subPath);
    }
}
