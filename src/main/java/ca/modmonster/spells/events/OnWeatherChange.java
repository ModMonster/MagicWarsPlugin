package ca.modmonster.spells.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class OnWeatherChange implements Listener {
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!event.getCause().equals(WeatherChangeEvent.Cause.NATURAL)) return;
        event.setCancelled(true);
    }
}
