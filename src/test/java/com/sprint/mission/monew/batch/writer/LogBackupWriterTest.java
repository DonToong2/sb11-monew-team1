package com.sprint.mission.monew.batch.writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.monew.batch.LogBackupDeleteFailedException;
import com.sprint.mission.monew.batch.LogBackupFailedException;
import com.sprint.mission.monew.batch.LogBackupMetrics;
import com.sprint.mission.monew.batch.dto.UploadPayload;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.Chunk;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
public class LogBackupWriterTest {

  @TempDir
  private Path tempDir;

  @Mock
  private S3Client s3Client;

  @Mock
  private LogBackupMetrics metrics;

  @InjectMocks
  private LogBackupWriter writer;

  private Path logFile;
  LocalDate yesterday;
  private UploadPayload payload;

  @BeforeEach
  void setUp() {
    yesterday = LocalDate.now().minusDays(1);
    logFile = tempDir.resolve("monew." + yesterday + ".log");
    payload = new UploadPayload(logFile, "key", "data".getBytes());
  }

  @Nested
  @DisplayName("백업 로그 파일 저장하기")
  class Writer {

    @Test
    @DisplayName("S3 업로드 실패 시 LogBackupFailedException 발생으로 Job이 실패한다")
    void S3_업로드_실패_시_LogBackupFailedException_발생() {
      // given
      given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
          .willThrow(new RuntimeException("S3 장애"));

      Chunk<UploadPayload> chunk = new Chunk<>(List.of(payload));

      // when & then
      assertThatThrownBy(() -> writer.write(chunk))
          .isInstanceOf(LogBackupFailedException.class);
    }

    @Test
    @DisplayName("로컬 파일 삭제 실패 시 LogBackupDeleteFailedException 발생으로 Job이 실패한다")
    void 로컬_파일_삭제_실패_시_Job_실패() throws Exception {
      // given
      Files.writeString(logFile, "log content");

      Chunk<UploadPayload> chunk = new Chunk<>(List.of(payload));

      try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
        filesMock.when(() -> Files.exists(any())).thenReturn(true);
        filesMock.when(() -> Files.readAllBytes(any())).thenReturn("log content".getBytes());
        filesMock.when(() -> Files.delete(any())).thenThrow(new IOException("삭제 실패"));

        // when & then
        assertThatThrownBy(() -> writer.write(chunk))
            .isInstanceOf(LogBackupDeleteFailedException.class);
      }
    }

    @Test
    @DisplayName("S3에 파일 업로드")
    void S3에_파일_업로드() throws Exception {
      // given
      Files.writeString(logFile, "log content");
      Chunk<UploadPayload> chunk = new Chunk<>(List.of(payload));

      // when
      writer.write(chunk);

      // then
      verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    @DisplayName("로그 파일이 존재하고 S3에 없으면 업로드 후 로컬 파일을 삭제한다")
    void 로그_파일이_존재하고_S3에_없으면_업로드_후_로컬_파일을_삭제한다() throws IOException {
      // given
      Files.writeString(logFile, "log content");
      Chunk<UploadPayload> chunk = new Chunk<>(List.of(payload));

      // when
      writer.write(chunk);

      // then
      verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
      assertThat(logFile).doesNotExist();
    }

    @Test
    @DisplayName("업로드 성공 시 업로드 건수·바이트·소요 시간을 집계한다")
    void 업로드_성공_시_업로드_건수_바이트_소요_시간을_집계한다() throws IOException {
      // given
      Files.writeString(logFile, "log content");
      Chunk<UploadPayload> chunk = new Chunk<>(List.of(payload));

      // when
      writer.write(chunk);

      // then
      verify(metrics).countUploaded();
      verify(metrics).recordBytes(anyLong());
    }
  }
}
