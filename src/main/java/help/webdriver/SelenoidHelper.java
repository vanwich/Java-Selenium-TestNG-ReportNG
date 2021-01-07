package help.webdriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.net.UrlEscapers;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.exceptions.JstrException;

public class SelenoidHelper {
    private static final Logger log = LoggerFactory.getLogger(SelenoidHelper.class);

    private final Optional<URL> hubUrl;
    private final Optional<String> authEncoded;
    private final Function<String, String> pathBuilder;

    public SelenoidHelper(Function<String, String> pathBuilder) {
        this.pathBuilder = pathBuilder;
        URL tmpUrl = null;
        String tmpAuthEncoded = null;

        try {
            tmpUrl = new URL(PropertyProvider.getProperty(TestProperties.WEBDRIVER_HUB_URL));
            if (tmpUrl.getPath().equals("/wd/hub")) {
                String auth = tmpUrl.getUserInfo();
                if (!StringUtils.isBlank(auth)) {
                    byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());
                    tmpAuthEncoded = new String(authEncBytes);
                }
            } else {
                log.error("URL " + tmpUrl + " is not a Selenoid/Ggr URL");
                tmpUrl = null;
            }
        } catch (MalformedURLException e) {
            log.warn("Failed to build hub URL", e);
            tmpUrl = null;
        }
        hubUrl = Optional.ofNullable(tmpUrl);
        authEncoded = Optional.ofNullable(tmpAuthEncoded);
    }

    protected Optional<URL> buildURL(String sessionId, String subPath) {
        return hubUrl.map(baseUrl -> {
            try {
                String path = pathBuilder.apply(sessionId);
                if (!StringUtils.isBlank(subPath)) {
                    path += "/" + UrlEscapers.urlFragmentEscaper().escape(subPath);
                }
                return new URL(baseUrl, path);
            } catch (MalformedURLException e) {
                throw new JstrException("Failed to build file download URL", e);
            }
        });
    }

    public <D> Optional<D> get(String sessionId, String subPath, Function<InputStream, D> streamProcessor) {
        return buildURL(sessionId, subPath).map(url -> {
            try {
                log.debug("Performing request to URL {}", url);
                InputStream is = buildUrlConnection(url).getInputStream();
                return streamProcessor.apply(is);
            } catch (IOException e) {
                log.warn("Failed to download file by URL {}. Original message {}", url, e.getMessage());
                return null;
            }
        });
    }

    public Optional<Boolean> delete(String sessionId, String subPath) {
        return buildURL(sessionId, subPath).map(url -> {
            try {
                HttpURLConnection urlConnection = buildUrlConnection(url);
                urlConnection.setRequestMethod("DELETE");
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    log.debug("DELETE request to URL {} successful", url);
                    return true;
                } else {
                    log.warn("DELETE request to URL {} produced response code {}", url, responseCode);
                    return false;
                }
            } catch (IOException e) {
                log.warn("Failed to delete by URL {}. Original message {}", url, e.getMessage());
                return null;
            }
        });
    }

    protected HttpURLConnection buildUrlConnection(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(PropertyProvider.getProperty(TestProperties.BROWSER_FILE_DOWNLOAD_TIMEOUT, 30000));
        authEncoded.ifPresent(auth -> urlConnection.setRequestProperty("Authorization", "Basic " + auth));
        return urlConnection;
    }

    public static Function<InputStream, File> buildFileGetter(String fileName, String targetDir) {
        return is -> {
            File file = new File(targetDir, fileName);
            try {
                FileUtils.copyInputStreamToFile(is, file);
                log.debug("Successfully copied file {} to directory {}", fileName, targetDir);
            } catch (IOException e) {
                log.warn("Failed to copy file {} to directory {}. Original message {}", fileName, targetDir, e.getMessage());
                return null;
            }
            return file;
        };
    }
}
