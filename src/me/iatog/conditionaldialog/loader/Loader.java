package me.iatog.conditionaldialog.loader;

public interface Loader {
	void load();
	default void unload() {
		// Empty method
	}
}
