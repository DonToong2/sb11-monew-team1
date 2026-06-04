package com.sprint.mission.monew.batch.reader;

import java.nio.file.Path;
import java.util.Iterator;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

public class LogFileReader implements ItemReader<Path> {

  @Value("${monew.log-dir}")
  private String logDir;

  private Iterator<Path> iterator;

  @Override
  public Path read() throws Exception {

    return null;
  }
}
