package com.sprint.mission.monew.batch.reader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

public class LogBackupReader implements ItemReader<Path> {

  @Value("${monew.log-dir}")
  private String logDir;

  private Iterator<Path> iterator;

  @Override
  public Path read() throws Exception {

    if (iterator == null) {

      try (Stream<Path> stream = Files.list(Path.of(logDir))) {
        iterator = stream
            .filter(Files::isRegularFile)
            .filter(p -> p.getFileName().toString().startsWith("monew."))
            .filter(p -> p.getFileName().toString().endsWith(".log"))
            .sorted()
            .toList()
            .iterator();
      }

    }

    return iterator.hasNext() ? iterator.next() : null;
  }
}
