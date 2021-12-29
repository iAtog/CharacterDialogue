package me.iatog.characterdialogue.api;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import me.iatog.characterdialogue.api.dialog.DialogHologram;

public class DialogHologramImpl implements DialogHologram {
	
	private boolean enabled;
	private float y;
	private List<String> lines;
	
	public DialogHologramImpl(boolean enabled, float y, @Nullable List<String> lines) {
		this.enabled = enabled;
		this.y = y;
		this.lines = lines;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public List<String> getLines() {
		return lines;
	}

}
