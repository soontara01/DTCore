package th.co.ais.dt.core.service.core.impl.sap.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SapCancelShoppingCartRequest {
    private String cancellationReason;
    private String effectiveCancellationDate;
    private String requestedCancellationDate;
    private ShoppingCart shoppingCart;

    @SerializedName("@type")
    private String type;
}
