/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebitsserver.odh.inventory;

import it.bz.opendatahub.alpinebits.xml.schema.ota.MessageAcknowledgementType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveContentNotifRS;
import it.bz.opendatahub.alpinebitsserver.application.common.utils.MessageAcknowledgementTypeBuilder;
import it.bz.opendatahub.alpinebitsserver.odh.backend.odhclient.dto.PushWrapper;
import it.bz.opendatahub.alpinebitsserver.odh.backend.odhclient.exception.OdhBackendException;
import it.bz.opendatahub.alpinebitsserver.odh.backend.odhclient.service.OdhBackendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service uses the ODH tourism data to store AlpineBits Inventory data.
 */
public class InventoryPushServiceImpl implements InventoryPushService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryPushServiceImpl.class);

    private final OdhBackendService service;

    public InventoryPushServiceImpl(OdhBackendService service) {
        this.service = service;
    }

    @Override
    public OTAHotelDescriptiveContentNotifRS writeBasic(PushWrapper pushWrapper) {
        try {
            service.pushInventoryBasic(pushWrapper);

            MessageAcknowledgementType mat = MessageAcknowledgementTypeBuilder.forSuccess();
            return new OTAHotelDescriptiveContentNotifRS(mat);
        } catch (OdhBackendException e) {
            LOG.error("ODH backend client error", e);
            String message = this.buildErrorMessage(e.getMessage(), pushWrapper.getRequestId());
            MessageAcknowledgementType mat = MessageAcknowledgementTypeBuilder.forError(message);
            return new OTAHotelDescriptiveContentNotifRS(mat);
        }
    }

    @Override
    public OTAHotelDescriptiveContentNotifRS writeHotelInfo(PushWrapper pushWrapper) {
        try {
            service.pushInventoryHotelInfo(pushWrapper);

            MessageAcknowledgementType mat = MessageAcknowledgementTypeBuilder.forSuccess();
            return new OTAHotelDescriptiveContentNotifRS(mat);
        } catch (OdhBackendException e) {
            LOG.error("ODH backend client error", e);
            String message = this.buildErrorMessage(e.getMessage(), pushWrapper.getRequestId());
            MessageAcknowledgementType mat = MessageAcknowledgementTypeBuilder.forError(message);
            return new OTAHotelDescriptiveContentNotifRS(mat);
        }
    }

    private String buildErrorMessage(String message, String requestId) {
        return message + " (rid = " + requestId + ")";
    }
}
