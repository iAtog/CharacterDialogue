package me.iatog.characterdialogue.loader.file;

import me.iatog.characterdialogue.ConditionalDialogPlugin;
import me.iatog.characterdialogue.libraries.FileFactoryImpl;
import me.iatog.characterdialogue.loader.Loader;

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
