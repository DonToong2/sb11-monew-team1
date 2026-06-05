package com.sprint.mission.monew.batch.reader;

import com.sprint.mission.monew.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCleanupReader implements ItemReader<User> {

  @Override
  public User read() {
    return null;
  }
}
