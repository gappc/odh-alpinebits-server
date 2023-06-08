// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebitsserver.odh.freerooms;

import it.bz.opendatahub.alpinebitsserver.odh.backend.odhclient.dto.PushWrapper;

/**
 * A service to handle an AlpineBits FreeRooms requests.
 * <p>
 * Note that AlpineBits FreeRooms supports push requests only.
 *
 * @param <T> The write response type. This type is generic,
 *            because AlpineBits 2020-10 changed the types used
 *            for FreeRooms.
 */
public interface FreeRoomsPushService<T> {

    /**
     * Write a FreeRooms information (up to AlpineBits 2018-10).
     *
     * @param pushWrapper this element contains the FreeRooms information
     *                    that should be written to ODH
     * @return The response to the FreeRooms write.
     */
    T write(PushWrapper pushWrapper);

}
