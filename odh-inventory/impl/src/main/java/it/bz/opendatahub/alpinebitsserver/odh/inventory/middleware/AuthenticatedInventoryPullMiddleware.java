/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebitsserver.odh.inventory.middleware;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsAction;
import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.common.exception.AlpineBitsException;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.xml.schema.v_2017_10.OTAHotelDescriptiveInfoRQ;
import it.bz.opendatahub.alpinebits.xml.schema.v_2017_10.OTAHotelDescriptiveInfoRS;
import it.bz.opendatahub.alpinebitsserver.odh.backend.odhclient.OdhBackendContextKey;
import it.bz.opendatahub.alpinebitsserver.odh.backend.odhclient.OdhBackendService;
import it.bz.opendatahub.alpinebitsserver.odh.inventory.impl.AuthenticatedInventoryPullServiceImpl;

/**
 * A {@link Middleware} to handle Inventory pull requests.
 *
 * This middleware is invoked, if the credentials provided by
 * the AlpineBits request are valid.
 */
public class AuthenticatedInventoryPullMiddleware implements Middleware {

    private final Key<OTAHotelDescriptiveInfoRQ> requestKey;
    private final Key<OTAHotelDescriptiveInfoRS> responseKey;

    public AuthenticatedInventoryPullMiddleware(
            Key<OTAHotelDescriptiveInfoRQ> requestKey,
            Key<OTAHotelDescriptiveInfoRS> responseKey
    ) {
        this.requestKey = requestKey;
        this.responseKey = responseKey;
    }

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        // Call service for persistence
        OTAHotelDescriptiveInfoRS response = this.invokeService(ctx);

        // Put result back into middleware context
        ctx.put(this.responseKey, response);
    }

    private OTAHotelDescriptiveInfoRS invokeService(Context ctx) {
        // Get necessary objects from middleware context
        String action = ctx.getOrThrow(RequestContextKey.REQUEST_ACTION);
        OdhBackendService odhBackendService = ctx.getOrThrow(OdhBackendContextKey.ODH_BACKEND_SERVICE);

        OTAHotelDescriptiveInfoRQ inventoryRequest = ctx.getOrThrow(requestKey);
        String hotelCode = inventoryRequest.getHotelDescriptiveInfos().getHotelDescriptiveInfo().getHotelCode();

        AuthenticatedInventoryPullServiceImpl service = new AuthenticatedInventoryPullServiceImpl(odhBackendService);

        // Call service for persistence
        if (AlpineBitsAction.INVENTORY_BASIC_PULL.equals(action)) {
            return service.readBasic(hotelCode);
        } else if (AlpineBitsAction.INVENTORY_HOTEL_INFO_PULL.equals(action)) {
            return service.readHotelInfo(hotelCode);
        }

        throw new AlpineBitsException("No implementation for action found", 500);
    }
}
