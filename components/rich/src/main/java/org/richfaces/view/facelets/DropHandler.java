/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.view.facelets;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.component.AbstractDropTarget;
import org.richfaces.event.MethodExpressionDropListener;

/**
 * @author abelevich
 *
 */
public class DropHandler extends ComponentHandler {
    private static final DropHandlerMetaRule METARULE = new DropHandlerMetaRule();

    public DropHandler(ComponentConfig config) {
        super(config);
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset m = super.createMetaRuleset(type);
        m.addRule(METARULE);
        return m;
    }

    static class DropHandlerMetaRule extends MetaRule {
        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AbstractDropTarget.class) && "dropListener".equals(name)) {
                return new DropTargetMapper(attribute);
            }
            return null;
        }
    }

    static class DropTargetMapper extends Metadata {
        private static final Class[] SIGNATURE = new Class[] { org.richfaces.event.DropEvent.class };
        private final TagAttribute attribute;

        DropTargetMapper(TagAttribute attribute) {
            this.attribute = attribute;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((AbstractDropTarget) instance).addDropListener((new MethodExpressionDropListener(this.attribute
                .getMethodExpression(ctx, null, SIGNATURE))));
        }
    }
}
