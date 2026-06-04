package com.sprint.mission.monew.batch.processor;

import com.sprint.mission.monew.batch.dto.UploadPayload;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.GZIPOutputStream;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class LogBackupProcessor implements ItemProcessor<Path, UploadPayload> {

  private static final DateTimeFormatter PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  @Override
  public UploadPayload process(Path file) throws Exception {
    LocalDate date = extractDate(file);

    String s3Key = "logs/" + date.format(PATH_FORMATTER)
        + "/app-" + date.format(FILE_FORMATTER) + ".log.gz";

    byte[] compressed = gzip(Files.readAllBytes(file));

    return new UploadPayload(file, s3Key, compressed);
  }

  private LocalDate extractDate(Path file) {
    String name = file.getFileName().toString();
    String dateStr = name.replace("monew.", "").replace(".log", "");
    return LocalDate.parse(dateStr);
  }

  private byte[] gzip(byte[] data) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (GZIPOutputStream gzos = new GZIPOutputStream(bos)) {
      gzos.write(data);
    }
    return bos.toByteArray();
  }
}
