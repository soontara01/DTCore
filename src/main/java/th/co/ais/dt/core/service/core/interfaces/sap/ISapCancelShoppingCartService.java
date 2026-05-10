package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartResponse;

@Transactional
public interface ISapCancelShoppingCartService {
    SapCancelShoppingCartResponse cancelShoppingCart(SapCancelShoppingCartRequest request);
}
