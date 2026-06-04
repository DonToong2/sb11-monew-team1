package com.sprint.mission.monew.batch.processor;

import com.sprint.mission.monew.batch.dto.UploadPayload;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import org.springframework.batch.item.ItemProcessor;

public class LogProcessor implements ItemProcessor<Path, UploadPayload> {

  private static final DateTimeFormatter PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  @Override
  public UploadPayload process(Path file) throws Exception {
    return null;
  }

}
