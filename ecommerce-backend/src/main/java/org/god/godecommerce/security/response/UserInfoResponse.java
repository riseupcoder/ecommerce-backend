package org.god.godecommerce.security.response;

import java.util.List;

public record UserInfoResponse(Long id, String userName, List<String> roles) {
}
