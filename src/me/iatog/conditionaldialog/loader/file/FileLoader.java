package me.iatog.conditionaldialog.loader.file;

import me.iatog.conditionaldialog.ConditionalDialogPlugin;
import me.iatog.conditionaldialog.libraries.FileFactoryImpl;
import me.iatog.conditionaldialog.loader.Loader;

public class FileLoader implements Loader {
	
	private ConditionalDialogPlugin main;
	
	public FileLoader(ConditionalDialogPlugin main) {
		this.main = main; 
	}
	
	@Override
	public void load() {
		main.setDefaultFileFactory(new FileFactoryImpl(main));
	}

}
