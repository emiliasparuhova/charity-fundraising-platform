package individual.userservice.controller.dto.user;

import individual.userservice.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class GetUsersResponse {
    private List<User> users;
}
