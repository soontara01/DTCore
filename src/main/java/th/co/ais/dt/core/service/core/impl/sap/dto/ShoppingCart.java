package th.co.ais.dt.core.service.core.impl.sap.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShoppingCart {
    private String id;

    @SerializedName("@type")
    private String type;
}
