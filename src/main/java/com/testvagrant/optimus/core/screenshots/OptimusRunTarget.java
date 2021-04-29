package com.testvagrant.optimus.core.screenshots;

import com.testvagrant.optimus.commons.filehandlers.GsonParser;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Set;

public class OptimusRunTarget {

  private OptimusRunContext optimusRunContext;
  private String screenshotDirPath, logDirPath;

  public OptimusRunTarget(OptimusRunContext optimusRunContext) {
    this.optimusRunContext = optimusRunContext;
    screenshotDirPath = createScreenShotDirectory(optimusRunContext);
    logDirPath = createLogsDirectory(optimusRunContext);
    saveTargetDetails();
  }

  public byte[] captureScreenshot() {
    return captureScreenshot(false);
  }

  public byte[] captureScreenshot(boolean returnImageInByteForm) {
    File file = takeScreenshotAsFile();
    Path desitnationPath = Paths.get(optimusRunContext.getTestFolder().toString(),
            "screenshots",
            LocalDateTime.now().toString()+".png");
    try {
      Files.move(file.toPath(), desitnationPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      //cannot move screenshot
    }
    if(returnImageInByteForm) return takeScreenshotAsBytes();
    return new byte[]{0};
  }


  public void captureLogs() {
    Set<String> availableLogTypes = null;
    try {
      availableLogTypes = optimusRunContext.getWebDriver().manage().logs().getAvailableLogTypes();
    } catch (Exception e) {
      return;
    }
    availableLogTypes.forEach(logType -> {
      LogEntries logEntries = optimusRunContext.getWebDriver().manage().logs().get(logType);
      FileWriter file = null;
      try {
        file = new FileWriter(Paths.get(logDirPath, logType+".json").toString());
        file.write(String.valueOf(logEntries.toJson()));
        file.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private void saveTargetDetails() {
    String serialize = GsonParser.toInstance().serialize(optimusRunContext.getTargets());
    FileWriter file = null;
    try {
      file = new FileWriter(Paths.get(optimusRunContext.getTestFolder().toString(), "target.json").toString());
      file.write(serialize);
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String createScreenShotDirectory(OptimusRunContext optimusRunContext) {
    Path screenshotDirPath = Paths.get(optimusRunContext.getTestFolder().toString(), "screenshots");
    return createDirectory(screenshotDirPath);
  }

  private String createLogsDirectory(OptimusRunContext optimusRunContext) {
    Path logDirPath = Paths.get(optimusRunContext.getTestFolder().toString(),
            "logs");
    return createDirectory(logDirPath);
  }

  private String createDirectory(Path dirPath) {
    File file = new File(dirPath.toString());
    if(file.exists() && file.isDirectory()) {
      try {
        FileUtils.deleteDirectory(file);
      } catch (IOException e) {
        // ignore exception on delete directory
      }
    }
    if(!file.exists()) file.mkdirs();
    return file.getAbsolutePath();
  }

  private File takeScreenshotAsFile() {
    return ((TakesScreenshot) optimusRunContext.getWebDriver()).getScreenshotAs(OutputType.FILE);
  }

  private byte[] takeScreenshotAsBytes() {
    return ((TakesScreenshot) optimusRunContext.getWebDriver()).getScreenshotAs(OutputType.BYTES);
  }
}
