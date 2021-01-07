package webdriver.video;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.rholder.retry.*;
import config.ClassConfigurator;
import config.PropertyProvider;
import config.TestProperties;
import webdriver.controls.waiters.Waiters;

public class VideoService {
    private static final Logger log = LoggerFactory.getLogger(VideoService.class);

    private static final boolean isEnabled = PropertyProvider.getProperty(TestProperties.ENABLE_VIDEO_RECORDING, false);

    @ClassConfigurator.Configurable(byClassName = true)
    private static IVideoManager videoManager;
    @ClassConfigurator.Configurable(byClassName = true)
    private static Supplier<WaitStrategy> waitStrategy = () -> WaitStrategies.fixedWait(15, TimeUnit.SECONDS);
    @ClassConfigurator.Configurable(byClassName = true)
    private static Supplier<StopStrategy> stopStrategy = () -> StopStrategies.stopAfterAttempt(4);

    @ClassConfigurator.Configurable(byClassName = true)
    private static Function<List<String>, Stream<String>> sessionVideoNamesProducer = sids -> sids.stream().map(sid -> sid + ".mp4");

    @ClassConfigurator.Configurable(byClassName = true)
    private static Supplier<Boolean> isVideoRecordingEnabled = () -> isEnabled;

    static {
        ClassConfigurator configurator = new ClassConfigurator(VideoService.class);
        configurator.applyConfiguration();

        if (videoManager == null) {
            if (StringUtils.isBlank(PropertyProvider.getProperty(TestProperties.WEBDRIVER_HUB_URL)) || !isEnabled) {
                videoManager = new NullVideoManager();
            } else {
                videoManager = new SelenoidVideoManager(System.getProperty("java.io.tmpdir"));
            }
        }
    }

    private VideoService() {}

    /**
     * Download videos for specified WebDriver sessions
     * @param videoNames list of video names
     * @return list of video files in local file system
     */
    public static List<File> getSessionVideos(List<String> videoNames) {
        return videoNames.stream()
                .flatMap(videoName -> {
                    Optional<File> o;
                    Waiters.SLEEP(PropertyProvider.getProperty(TestProperties.TEST_VIDEO_DOWNLOAD_DELAY, 10000L)).go();
                    try {
                        log.debug("Getting video for session {}", videoName);
                        o = RetryerBuilder.<Optional<File>>newBuilder()
                                .retryIfResult(r -> !r.isPresent())
                                .withWaitStrategy(waitStrategy.get())
                                .withStopStrategy(stopStrategy.get())
                                .build()
                                .call(() -> videoManager.get(videoName));
                        log.debug("Finished getting video {}", videoName);
                    } catch (RetryException | ExecutionException e) {
                        log.warn("Failed to get video {}", videoName, e);
                        o = Optional.empty();
                    }
                    return o.isPresent() ? Stream.of(o.get()) : Stream.empty();
                }).collect(Collectors.toList());
    }

    /**
     * Delete videos for currently stored WebDriver sessions on remote WebDriver node
     * @param videoNames list of video names
     */
    public static void deleteSessionVideos(List<String> videoNames) {
        videoNames.stream().forEach(videoManager::delete);
    }

    /**
     * Get stream of video file names corresponding to provided session ids
     * @param sids list of session ids
     * @return stream of video names corresponding to specified session ids
     */
    public static Stream<String> getSessionVideoNames(List<String> sids) {
        return sessionVideoNamesProducer.apply(sids);
    }

    /**
     * Check whether video recording is enabled. Default implementation simply checks {@link TestProperties#ENABLE_VIDEO_RECORDING} property
     * but configurable field {@link #isVideoRecordingEnabled} allows to implement selective recording, e.g. depending on current test method.
     * @return true if video recording is enabled.
     */
    public static boolean isVideoRecordingEnabled() {
        return isVideoRecordingEnabled.get();
    }
}
