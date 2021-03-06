/*
 * blue - object composition environment for csound
 * Copyright (C) 2014
 * Steven Yi <stevenyi@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package blue.plugin.processors;

import blue.plugin.ProjectPluginEditorItem;
import blue.plugin.ScoreObjectEditorPlugin;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import org.openide.filesystems.annotations.LayerBuilder.File;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author stevenyi
 */
@ServiceProvider(service = Processor.class)
@SupportedAnnotationTypes("blue.plugin.ProjectPluginEditorItem")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ProjectPluginEditorItemProcessor extends LayerGeneratingProcessor {

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> set, RoundEnvironment env)
            throws LayerGenerationException {
        Elements elements = processingEnv.getElementUtils();
        for (Element e : env.getElementsAnnotatedWith(ProjectPluginEditorItem.class)) {
            TypeElement clazz = (TypeElement) e;
            String teName = elements.getBinaryName(clazz).toString();
            ProjectPluginEditorItem ppePlugin = 
                    clazz.getAnnotation(ProjectPluginEditorItem.class);
            File f = layer(e).file(
                    "blue/project/plugins/editors/" + teName.replace('.', '-') + ".instance")
                    .intvalue("position", ppePlugin.position()).
                    bundlevalue("displayName", ppePlugin.displayName());
            f.write();
        }
        return true;
    }

}
