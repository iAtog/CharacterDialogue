package me.iatog.characterdialogue.libraries;

import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

// this file may become absurdly large

public class YamlVersions {

    public static class ConfigVersion implements FileVersion {

        @Override
        public LoaderSettings getLoaderSettings() {
            return LoaderSettings.builder()
                    .setAutoUpdate(true)
                    .setCreateFileIfAbsent(true)
                    .build();
        }

        @Override
        public UpdaterSettings getUpdaterSettings() {
            return UpdaterSettings.builder()
                    .setAutoSave(true)
                    .setVersioning(new BasicVersioning("file-version"))

                    .addIgnoredRoute("1.0.0", "npc", '.')


                    .build();
        }
    }

    public static class LanguageVersion implements FileVersion {

        @Override
        public LoaderSettings getLoaderSettings() {
            return null;
        }

        @Override
        public UpdaterSettings getUpdaterSettings() {
            return null;
        }
    }

    public interface FileVersion {
        LoaderSettings getLoaderSettings();
        UpdaterSettings getUpdaterSettings();
    }

}
