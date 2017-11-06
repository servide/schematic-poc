package io.servide.schematic;

import com.google.inject.Binder;

import net.voldex.common.spigot.inject.plugin.ServidePlugin;
import net.voldex.common.spigot.plugin.auto.Plugin;

@Plugin(name = "SchematicPOC")
public class Schempoc extends ServidePlugin {

	@Override
	public void enable()
	{
		this.getDataFolder().mkdirs();
	}

	@Override
	public void configure(Binder binder)
	{

	}

}
