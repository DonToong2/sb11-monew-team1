package com.sprint.mission.monew.batch.writer;

import com.sprint.mission.monew.batch.LogBackupMetrics;
import com.sprint.mission.monew.batch.dto.UploadPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Component
@RequiredArgsConstructor
public class LogBackupWriter implements ItemWriter<UploadPayload> {

  private final S3Client s3Client;
  private final LogBackupMetrics metrics;

  @Override
  public void write(Chunk<? extends UploadPayload> chunk) {
  }

}
