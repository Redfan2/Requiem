/*
 * Requiem
 * Copyright (C) 2017-2024 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package ladysnake.requiem.api.v1.remnant;

import baritone.api.fakeplayer.FakeServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Represents the result of a successful {@link RemnantComponent#splitPlayer(boolean) splitting operation}
 *
 * @param soul  the vagrant form of the split player, controlled by the client
 * @param shell the physical shell left behind, implemented as a fake player
 */
public record PlayerSplitResult(ServerPlayerEntity soul, FakeServerPlayerEntity shell) {

}
