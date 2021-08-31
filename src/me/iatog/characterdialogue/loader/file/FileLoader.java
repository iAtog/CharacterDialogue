package me.iatog.characterdialogue.loader.file;

import me.iatog.characterdialogue.CharacterDialogPlugin;
import me.iatog.characterdialogue.libraries.FileFactoryImpl;
import me.iatog.characterdialogue.loader.Loader;

public class FileLoader implements Loader {
	
	private CharacterDialogPlugin main;
	
	public FileLoader(CharacterDialogPlugin main) {
		this.main = main; 
	}
	
	@Override
	public void load() {
		main.setDefaultFileFactory(new FileFactoryImpl(main));
	}

}
