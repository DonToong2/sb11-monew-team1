package com.sprint.mission.monew.batch.reader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class LogBackupReader implements ItemReader<Path> {

  @Value("${monew.log-dir}")
  private String logDir;

  private boolean read = false;

  @Override
  public Path read() {

    if (read) {
      return null;
    }

    read = true;

    LocalDate yesterday = LocalDate.now().minusDays(1);

    Path file = Path.of(logDir, "monew." + yesterday + ".log");

    if (!Files.exists(file)) {
      return null;
    }
    return file;
  }
}
