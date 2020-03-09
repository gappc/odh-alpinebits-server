/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebitsserver.application.common.routing;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCapability;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.housekeeping.middleware.HousekeepingGetCapabilitiesMiddleware;
import it.bz.opendatahub.alpinebits.housekeeping.middleware.HousekeepingGetVersionMiddleware;
import it.bz.opendatahub.alpinebits.routing.DefaultRouter;
import it.bz.opendatahub.alpinebits.routing.RoutingBuilder;
import it.bz.opendatahub.alpinebits.routing.constants.Action;
import it.bz.opendatahub.alpinebitsserver.odh.freerooms.v_2017_10.FreeRoomsPushMiddlewareBuilder;
import it.bz.opendatahub.alpinebitsserver.odh.inventory.v_2017_10.InventoryPullMiddlewareBuilder;
import it.bz.opendatahub.alpinebitsserver.odh.inventory.v_2017_10.InventoryPushMiddlewareBuilder;

import javax.xml.bind.JAXBException;

/**
 * Route definitions for AlpineBits 2017-10.
 */
public final class RoutesFor201710 {

    private RoutesFor201710() {
        // Empty
    }

    /**
     * Add 2017-10 routes to the given builder.
     *
     * @param builder The routes will be added to this builder.
     * @return A {@link RoutingBuilder.FinalBuilder} that can be used for further route building.
     * @throws JAXBException If there was an error creating any of the routes that rely on
     *                       XML processing.
     */
    public static RoutingBuilder.FinalBuilder routes(DefaultRouter.Builder builder) throws JAXBException {
        return builder.version(AlpineBitsVersion.V_2017_10)
                .supportsAction(Action.GET_VERSION)
                .withCapabilities(AlpineBitsCapability.GET_VERSION)
                .using(new HousekeepingGetVersionMiddleware())
                .and()
                .supportsAction(Action.GET_CAPABILITIES)
                .withCapabilities(AlpineBitsCapability.GET_CAPABILITIES)
                .using(new HousekeepingGetCapabilitiesMiddleware())
                .and()
                .supportsAction(Action.INVENTORY_BASIC_PULL)
                .withCapabilities(AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_INFO_INVENTORY)
                .using(InventoryPullMiddlewareBuilder.buildInventoryPullMiddleware())
                .and()
                .supportsAction(Action.INVENTORY_HOTEL_INFO_PULL)
                .withCapabilities(AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_INFO_INFO)
                .using(InventoryPullMiddlewareBuilder.buildInventoryPullMiddleware())
                .and()
                .supportsAction(Action.INVENTORY_BASIC_PUSH)
                .withCapabilities(
                        AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INVENTORY,
                        AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INVENTORY_USE_ROOMS
                )
                .using(InventoryPushMiddlewareBuilder.buildInventoryPushMiddleware())
                .and()
                .supportsAction(Action.INVENTORY_HOTEL_INFO_PUSH)
                .withCapabilities(AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INFO)
                .using(InventoryPushMiddlewareBuilder.buildInventoryPushMiddleware())
                .and()
                .supportsAction(Action.FREE_ROOMS_HOTEL_AVAIL_NOTIF_FREE_ROOMS)
                .withCapabilities(
                        AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF,
                        AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_ROOMS
                )
                .using(FreeRoomsPushMiddlewareBuilder.buildFreeRoomsPushMiddleware())
                .versionComplete();
    }
}