package com.github.konifar.gradle.remover

import com.github.konifar.gradle.remover.remover.filetype.*
import com.github.konifar.gradle.remover.remover.valuetype.*
import com.github.konifar.gradle.remover.util.ColoredLogger
import org.gradle.api.Plugin
import org.gradle.api.Project

class UnusedResourcesRemoverPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create(UnusedResourcesRemoverExtension.NAME, UnusedResourcesRemoverExtension)

        project.task("removeUnusedResources").doLast {

            def extension = project.extensions.findByName(UnusedResourcesRemoverExtension.NAME) as UnusedResourcesRemoverExtension

            logExtensionInfo(extension)

            try {

                // Remove unused files
                [
                        new LayoutFileRemover(),
                        new MenuFileRemover(),
                        new MipmapFileRemover(),
                        new DrawableFileRemover(),
                        new AnimatorFileRemover(),
                        new AnimFileRemover(),
                        new ColorFileRemover(),
                ].forEach {
                    it.remove(project, extension)
                }

                // Remove unused xml values
                [
                        new ThemeXmlValueRemover(),
                        new StyleXmlValueRemover(),
                        new StringXmlValueRemover(),
                        new DimenXmlValueRemover(),
                        new ColorXmlValueRemover(),
                        new IntegerXmlValueRemover(),
                        new BoolXmlValueRemover(),
                        new IdXmlValueRemover(),
                        new AttrXmlValueRemover(),
                ].forEach {
                    it.remove(project, extension)
                }

                // Remove files or xml values in extra setting
                extension.extraRemovers.forEach {
                    it.remove(project, extension)
                }

            } catch (Exception e) {
                e.printStackTrace()
            } finally {
                ColoredLogger.println "\nIf you have any problems, please create an issue on GitHub."
                ColoredLogger.println "https://github.com/konifar/gradle-unused-resources-remover-plugin/issues/new"
            }

        }
    }

    static void logExtensionInfo(UnusedResourcesRemoverExtension extension) {
        if (extension.extraRemovers.size() > 0) {
            ColoredLogger.log "extraRemovers:"
            extension.extraRemovers.forEach {
                ColoredLogger.log "  ${it.toString()}"
            }
        }

        if (extension.excludeModules.size() > 0) {
            ColoredLogger.log "excludeModules:"
            extension.excludeModules.forEach {
                ColoredLogger.log "  ${it}"
            }
        }

        if (extension.excludeNames.size() > 0) {
            ColoredLogger.log "excludeNames:"
            extension.excludeNames.forEach {
                ColoredLogger.log "  ${it}"
            }
        }

        ColoredLogger.log "dryRun: ${extension.dryRun}"
    }

}