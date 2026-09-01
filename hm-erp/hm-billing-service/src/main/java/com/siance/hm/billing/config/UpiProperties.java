package com.siance.hm.billing.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "hm.billing.upi")
public class UpiProperties {

    /** Default payee VPA used when a request doesn't override it - e.g. "hospital@upi". */
    private String defaultPayeeVpa = "hospital@upi";

    private String defaultPayeeName = "Hospital";

    /** Public QR-image generation endpoint - swap for a self-hosted generator if you'd rather not call out. */
    private String qrImageBaseUrl = "https://api.qrserver.com/v1/create-qr-code/";
}
