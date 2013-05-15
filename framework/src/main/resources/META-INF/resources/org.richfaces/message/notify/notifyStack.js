/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

(function($, rf) {
    
    rf.ui = rf.ui || {};
    
    var defaultOptions = {
        position: "tr",
        direction: "vertical",
        method: "last",
        notifications: [],
        addNotification: function(pnotify) {
            this.notifications.push(pnotify);
        }
    };

    rf.ui.NotifyStack = rf.BaseComponent.extendClass({
        
        name : "NotifyStack",

        init : function(componentId, options) {
            $super.constructor.call(this, componentId);
            this.attachToDom(this.id);
            this.__initializeStack(options);
        },
        
        __initializeStack : function(options) {
            var stack = $.extend({}, $.pnotify.defaults.pnotify_stack, defaultOptions, options);
            
            var isVertical = (stack.direction == 'vertical');
            var isFirst = (stack.method == 'first');
            
            stack.push = isFirst ? 'top' : 'bottom';
            
            switch (stack.position) {
                case "tl": // topLeft
                    stack.dir1 = isVertical ? 'down' : 'right';
                    stack.dir2 = isVertical ? 'right' : 'down';
                    break;
                case "tr": // topRight
                    stack.dir1 = isVertical ? 'down' : 'left';
                    stack.dir2 = isVertical ? 'left' : 'down';
                    break;
                case "bl": // bottomLeft
                    stack.dir1 = isVertical ? 'up' : 'right';
                    stack.dir2 = isVertical ? 'right' : 'up';
                    break;
                case "br": // bottomRight
                    stack.dir1 = isVertical ? 'up' : 'left';
                    stack.dir2 = isVertical ? 'left' : 'up';
                    break;
                default:
                    throw "wrong stack position: " + stack.position;
            }
            
            this.stack = stack;
        },
    
        getStack : function() {
            return this.stack;
        },
        
        removeNotifications: function() {
            var pnotify;
            while (pnotify = this.stack.notifications.pop()) {
                pnotify.pnotify_remove();
            }
        },
        
        destroy : function() {
            this.removeNotifications();
            this.stack = null;
            $super.destroy.call(this);
        }
    });
    
    var $super = rf.ui.NotifyStack.$super;

})(jQuery, RichFaces);