package thi.hexa.userservice.adapter.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdsResponse {
    private List<Integer> user_ids;
}
