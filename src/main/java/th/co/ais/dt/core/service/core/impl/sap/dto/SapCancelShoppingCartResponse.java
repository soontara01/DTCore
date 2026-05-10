package th.co.ais.dt.core.service.core.impl.sap.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SapCancelShoppingCartResponse {
    private String cancellationReason;
    private String effectiveCancellationDate;
    private String requestedCancellationDate;
    private ShoppingCart shoppingCart;

    @SerializedName("@type")
    private String type;

    // Response error
    private String status;
    private String code;
    private String reason;
    private String message;

}
