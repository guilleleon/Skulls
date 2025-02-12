/*
 * Skulls
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.skulls.impl.economy;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.skulls.exception.CurrencyNotFoundException;
import lombok.NonNull;
import me.TechsCode.UltraEconomy.UltraEconomy;
import me.TechsCode.UltraEconomy.objects.Account;
import org.bukkit.entity.Player;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

public final class CoinsEngineEconomy extends MultiCurrencyEconomy {

    private final Currency currency;

    public CoinsEngineEconomy(@NonNull final String currentName) {
        this.currencyName = currentName;
        this.currency = CoinsEngineAPI.getCurrency(this.currencyName);
        if (this.currency == null)
            throw new CurrencyNotFoundException("Could not find the currency: '" + this.currencyName + "' from " + this.getName() + ", please check spelling or if it even exists.");

        Common.log("&aSetting up coinsengine economy provider");
    }

    @Override
    public String getName() {
        return "CoinsEngine";
    }

    @Override
    public boolean requiresExternalPlugin() {
        return true;
    }

    @Override
    public boolean has(@NonNull Player player, double amount) {
        if (currency == null || this.currencyName == null)
            return false;

        double balance = CoinsEngineAPI.getBalance(player, this.currency);

        return balance >= amount;
    }

    @Override
    public void withdraw(@NonNull Player player, double amount) {
        if (currency == null || this.currencyName == null)
            return;

        CoinsEngineAPI.removeBalance(player, this.currency, amount);
    }

    @Override
    public void deposit(@NonNull Player player, double amount) {
        if (currency == null || this.currencyName == null)
            return;

        CoinsEngineAPI.addBalance(player, this.currency, amount);
    }
}
