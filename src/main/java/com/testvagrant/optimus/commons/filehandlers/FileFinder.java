package com.testvagrant.optimus.commons.filehandlers;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {

  private String path;
  private boolean fileFound;
  private final List<File> allFiles;
  private File fileToSearch;

  private FileFinder(String path) {
    this.path = path;
    allFiles = new ArrayList<>();
  }

  public static FileFinder fileFinder(String rootPath) {
    return new FileFinder(rootPath);
  }

  public static FileFinder fileFinder() {
    return new FileFinder(System.getProperty("user.dir"));
  }

  public List<File> find(String fileExtension) {
    collectFiles(new File(path), fileExtension);
    return allFiles;
  }

  public File find(String fileName, String fileExtension) {
    if (fileName.contains("/")) {
      String[] paths = fileName.split("/");
      fileName = paths[paths.length - 1];
      path = Paths.get(path, paths).toString().replace(fileName, "");
    }
    collectFile(new File(path), fileName, fileExtension);
    return fileToSearch;
  }

  private void collectFiles(File rootFile, String fileExtensionToSearch) {
    if (rootFile.exists() && rootFile.isDirectory()) {
      File[] files = rootFile.listFiles();
      assert files != null;
      for (File file : files) {
        if (!file.isDirectory()) {
          fileFound = file.getName().endsWith(fileExtensionToSearch);
          if (fileFound) {
            allFiles.add(file);
          }
        } else {
          collectFiles(file, fileExtensionToSearch);
        }
      }
    }
  }

  private void collectFile(File rootFile, String fileName, String fileExtensionToSearch) {
    if (rootFile.exists() && rootFile.isDirectory()) {
      File[] files = rootFile.listFiles();
      assert files != null;
      for (File file : files) {
        if (!file.isDirectory()) {
          fileFound = file.getName().equals(String.format("%s%s", fileName, fileExtensionToSearch));
          if (fileFound) {
            fileToSearch = file;
            break;
          }
        } else {
          collectFile(file, fileName, fileExtensionToSearch);
        }
      }
    }
  }
}
