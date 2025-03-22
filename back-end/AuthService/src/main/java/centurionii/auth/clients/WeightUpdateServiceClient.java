package centurionii.auth.clients;

import centurionii.auth.utils.dto.WeightUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "weight-update-service", url = "${weight-update-service.url}")
public interface WeightUpdateServiceClient {
    @PostMapping("/weight-updates/initial")
    void createInitialWeight(@RequestBody WeightUpdateRequest request);
}
