package me.iatog.characterdialogue.libraries;

import dev.dejvokep.boostedyaml.dvs.Pattern;
import dev.dejvokep.boostedyaml.dvs.segment.Segment;
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
            //Pattern pattern = new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10));
            return UpdaterSettings.builder()
                    .setAutoSave(true)
                    .setVersioning(new BasicVersioning("file-version"))
                    //.setVersioning(pattern, "use-actionbar")

                    .addIgnoredRoute("1", "placeholders", '.')
                    .addIgnoredRoute("1", "npc", '.')


                    .build();
        }
    }

    public static class LanguageVersion implements FileVersion {

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

                    .build();
        }
    }

    public interface FileVersion {
        LoaderSettings getLoaderSettings();
        UpdaterSettings getUpdaterSettings();
    }

}
