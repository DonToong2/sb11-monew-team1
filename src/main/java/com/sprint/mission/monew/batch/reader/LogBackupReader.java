package com.sprint.mission.monew.batch.reader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogBackupReader implements ItemReader<Path> {

  @Value("${monew.log-dir}")
  private String logDir;

  private Iterator<Path> iterator;

  @Override
  public Path read() throws Exception {

    if (iterator == null) {
      List<Path> files;

      try (Stream<Path> stream = Files.list(Path.of(logDir))) {
        files = stream
            .filter(Files::isRegularFile)
            .filter(p -> p.getFileName().toString().startsWith("monew."))
            .filter(p -> p.getFileName().toString().endsWith(".log"))
            .sorted()
            .toList();
      }
      iterator = files.iterator();
    }

    return iterator.hasNext() ? iterator.next() : null;
  }
}
