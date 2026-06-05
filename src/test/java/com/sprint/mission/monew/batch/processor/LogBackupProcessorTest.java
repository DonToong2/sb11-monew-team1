package com.sprint.mission.monew.batch.processor;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.batch.dto.UploadPayload;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogBackupProcessorTest {

  @TempDir
  Path tempDir;

  @InjectMocks
  private LogBackupProcessor processor;

  private LocalDate yesterday;
  private Path file;

  @BeforeEach
  void setUp() {
    yesterday = LocalDate.now().minusDays(1);
    file = tempDir.resolve("monew." + yesterday + ".log");
  }

  @Nested
  @DisplayName("백업 로그 파일 변환하기")
  class Processor {

    @Test
    @DisplayName("백업 로그 파일 변환")
    void 백업_로그_파일_변환() throws Exception {
      // given
      // BeforeEach에서 로그 파일 초기화
      Files.writeString(file, "log content");

      // when
      UploadPayload result = processor.process(file);

      // then

      // UploadPayLoad 검증
      assertThat(result).isNotNull();
      assertThat(result.logFile()).isEqualTo(file);
      assertThat(result.s3Key()).isNotBlank();
      assertThat(result.compressedData()).isNotEmpty();

      // S3 key 검증
      assertThat(result.s3Key()).contains("logs/").contains(String.valueOf(yesterday.getYear()));

      String expectedKey =
          "logs/" + yesterday.format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd")) +
              "/app-" + yesterday.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) +
              ".log.gz";
      
      assertThat(result.s3Key()).isEqualTo(expectedKey);
      assertThat(result.s3Key()).contains("logs/").contains(String.valueOf(yesterday.getYear()));

      // gzip로 압축됐는지 검증
      assertThat(result.compressedData()).isNotEmpty();
    }
  }
}
