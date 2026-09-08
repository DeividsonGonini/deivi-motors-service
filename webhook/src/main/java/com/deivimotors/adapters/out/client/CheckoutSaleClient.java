package com.deivimotors.adapters.out.client;

import com.deivimotors.adapters.out.client.request.CheckoutSaleRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "CheckoutSaleClient",
        url = "${deivi-motors-service.checkout-payment.url}"
)
public interface CheckoutSaleClient {

    @PostMapping("/deivi_motors/v1/payments/sale/{saleId}/checkout")
    void checkoutSale(
            @PathVariable("saleId") UUID saleId,
            @RequestBody CheckoutSaleRequest request
    );


}
