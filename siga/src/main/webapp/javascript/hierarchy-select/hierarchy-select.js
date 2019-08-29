// From: https://github.com/NeoFusion/hierarchy-select
// License: MIT

(function($){
    'use strict';

    var HierarchySelect = function(element, options) {
        this.$element = $(element);
        this.options = $.extend({}, $.fn.hierarchySelect.defaults, options);
        this.$button = this.$element.children('button');
        this.$selectedLabel = this.$button.children('.selected-label');
        this.$menu = this.$element.children('.dropdown-menu');
        this.$menuInner = this.$menu.children('.inner');
        this.$searchbox = this.$menu.find('input');
        this.$hiddenField = this.$element.children('input');
        this.previouslySelected = null;
        this.init();
    };

    HierarchySelect.prototype = {
        constructor: HierarchySelect,
        init: function() {
            this.setWidth();
            this.setHeight();
            this.initSelect();
            this.clickListener();
            this.buttonListener();
            this.searchListener();
        },
        initSelect: function() {
            var items = this.$menuInner.find('li[data-group]').addClass('group disabled');

            var item = this.$menuInner.find('li[data-default-selected]:first');
            if (item.length) {
                this.setValue(item.data('value'));
            } else {
                var firstItem = this.$menuInner.find('li:first');
                this.setValue(firstItem.data('value'));
            }
        },
        setWidth: function() {
            if (this.options.width === 'auto') {
                var width = this.$menu.width();
                this.$element.css('min-width', width + 2 + 'px');
            } else if (this.options.width) {
                this.$element.css('width', this.options.width);
            } else {
                this.$element.css('min-width', '42px');
            }
        },
        setHeight: function() {
            if (this.options.height) {
                this.$menu.css('overflow', 'hidden');
                this.$menuInner.css({
                    'max-height': this.options.height,
                    'overflow-y': 'auto'
                });
            }
        },
        getText: function() {
            return this.$selectedLabel.text();
        },
        getValue: function() {
            return this.$hiddenField.val();
        },
        setValue: function(value) {
            var li = this.$menuInner.children('li[data-value="' + value + '"]:first');
            this.setSelected(li);
        },
        enable: function() {
            this.$button.removeAttr('disabled');
        },
        disable: function() {
            this.$button.attr('disabled', 'disabled');
        },
        setSelected: function(li) {
            if (li.length) {
                var text = li.children('a').text().substring(0,li.children('a').text().length - li.children('a').children('small').text().length);
                var value = li.data('value');
                this.$selectedLabel.html(text);
                this.$hiddenField.val(value).change();
                this.$menuInner.find('.active').removeClass('active');
                li.addClass('active');
                this.$element.trigger('change');
            }
        },
        moveUp: function () {
            var items = this.$menuInner.find('li:not(.hidden,.disabled)');
            var liActive = this.$menuInner.find('.active');
            var index = items.index(liActive);
            if (typeof items[index - 1] !== 'undefined') {
                this.$menuInner.find('.active').removeClass('active');
                items[index - 1].classList.add('active');
                processElementOffset(this.$menuInner[0], items[index - 1]);
            }
        },
        moveDown: function () {
            var items = this.$menuInner.find('li:not(.hidden,.disabled)');
            var liActive = this.$menuInner.find('.active');
            var index = items.index(liActive);
            if (typeof items[index + 1] !== 'undefined') {
                this.$menuInner.find('.active').removeClass('active');
                if (items[index + 1]) {
                    items[index + 1].classList.add('active');
                    processElementOffset(this.$menuInner[0], items[index + 1]);
                }
            }
        },
        selectItem: function () {
            var that = this;
            var selected = this.$menuInner.find('.active');
            if (selected.hasClass('hidden') || selected.hasClass('disabled')) {
                return;
            }
            setTimeout(function() {
                that.$button.focus();
            }, 0);
            selected && this.setSelected(selected);
            this.$button.dropdown('toggle');
        },
        clickListener: function() {
            var that = this;
            this.$element.on('show.bs.dropdown', function() {
                var $this = $(this);
                var scrollTop = $(window).scrollTop();
                var windowHeight = $(window).height();
                var upperHeight = $this.offset().top - scrollTop;
                var elementHeight = $this.outerHeight();
                var lowerHeight = windowHeight - upperHeight - elementHeight;
                var dropdownHeight = that.$menu.outerHeight(true);
                if (lowerHeight < dropdownHeight && upperHeight > dropdownHeight) {
                    $this.toggleClass('dropup', true);
                }
                var selected = that.$menuInner.find('.active');
                selected && setTimeout(function() {
                    var el = selected[0];
                    var p = selected[0].parentNode;
                    if (!(p.scrollTop <= el.offsetTop && (p.scrollTop + p.clientHeight) > el.offsetTop + el.clientHeight)) {
                        el.parentNode.scrollTop = el.offsetTop
                    }
                }, 0);
            });
            this.$element.on('shown.bs.dropdown', function() {
                that.previouslySelected = that.$menuInner.find('.active');
                that.$searchbox.focus();
            });
            this.$element.on('hidden.bs.dropdown', function() {
                that.$element.toggleClass('dropup', false);
            });
            this.$menuInner.on('click', 'li a', function (e) {
                e.preventDefault();
                var $this = $(this);
                var li = $this.parent();
                if (li.hasClass('disabled')) {
                    e.stopPropagation();
                } else {
                    that.setSelected(li);
                }
            });
        },
        buttonListener: function () {
            var that = this;
            if (this.options.search) {
                return;
            }
            this.$button.on('keydown', function (e) {
                switch (e.keyCode) {
                    case 9: // Tab
                        if (that.$element.hasClass('open')) {
                            e.preventDefault();
                        }
                        break;
                    case 13: // Enter
                        if (that.$element.hasClass('open')) {
                            e.preventDefault();
                            that.selectItem();
                        }
                        break;
                    case 27: //Esc
                        if (that.$element.hasClass('open')) {
                            e.preventDefault();
                            e.stopPropagation();
                            that.$button.focus();
                            that.previouslySelected && that.setSelected(that.previouslySelected);
                            that.$button.dropdown('toggle');
                        }
                        break;
                    case 38: // Up
                        if (that.$element.hasClass('open')) {
                            e.preventDefault();
                            e.stopPropagation();
                            that.moveUp();
                        }
                        break;
                    case 40: // Down
                        if (that.$element.hasClass('open')) {
                            e.preventDefault();
                            e.stopPropagation();
                            that.moveDown();
                        }
                        break;
                    default:
                        break;
                }
            })
        },
        searchListener: function() {
            var that = this;
            if (!this.options.search) {
                this.$searchbox.parent().toggleClass('hidden', true);
                return;
            }
            function disableParents(element) {
                var item = element;
                var level = item.data('level');
                while (typeof item == 'object' && item.length > 0 && level > 1) {
                    level--;
                    item = item.prevAll('li[data-level="' + level + '"]:first');
                    if (item.hasClass('hidden')) {
                        item.toggleClass('disabled', true);
                        item.removeClass('hidden');
                    }
                }
            }
            this.$searchbox.on('keydown', function (e) {
                switch (e.keyCode) {
                    case 9: // Tab
                        e.preventDefault();
                        e.stopPropagation();
                        that.$menuInner.click();
                        that.$button.focus();
                        break;
                    case 13: // Enter
                        that.selectItem();
                        break;
                    case 27: // Esc
                        e.preventDefault();
                        e.stopPropagation();
                        that.$button.focus();
                        that.previouslySelected && that.setSelected(that.previouslySelected);
                        that.$button.dropdown('toggle');
                        break;
                    case 38: // Up
                        e.preventDefault();
                        that.moveUp();
                        break;
                    case 40: // Down
                        e.preventDefault();
                        that.moveDown();
                        break;
                    default:
                        break;
                }
            });
            this.$searchbox.on('input propertychange', function (e) {
                e.preventDefault();
                var searchString = that.$searchbox.val().toLowerCase();
                var items = that.$menuInner.find('li');
                if (searchString.length === 0) {
                    items.each(function() {
                        var item = $(this);
                        if (!item.hasClass('group'))
                        	item.toggleClass('disabled', false);
                        item.toggleClass('hidden', false);
                    });
                } else {
                    items.each(function() {
                        var item = $(this);
                        var text = item.data('search').toLowerCase();
                        if (!text)
                        	text = item.children('a').text().toLowerCase();
                        searchString = accentsTidy(searchString);
                        var arr = searchString.replace(new RegExp("\\s{2,}", 'g'), " ").trim().split(' ');
                        var found = true 
                        for (var i = 0; i < arr.length; i++) {
                        	if (text.indexOf(arr[i]) == -1) {
                        		found = false;
                        		break;
                        	}
                        }
                        if (found) {
                            if (!item.hasClass('group'))
                            	item.toggleClass('disabled', false);
                            item.toggleClass('hidden', false);
                            if (that.options.hierarchy) {
                                disableParents(item);
                            }
                        } else {
                            if (!item.hasClass('group'))
                            	item.toggleClass('disabled', false);
                            item.toggleClass('hidden', true);
                        }
                    });
                }
            });
        }
    };
    
    var Plugin = function(option) {
        var args = Array.prototype.slice.call(arguments, 1);
        var method;
        var chain = this.each(function() {
            var $this   = $(this);
            var data    = $this.data('HierarchySelect');
            var options = typeof option == 'object'  && option;
            if (!data) {
                $this.data('HierarchySelect', (data = new HierarchySelect(this, options)));
            }
            if (typeof option == 'string') {
                method = data[option].apply(data, args);
            }
        });

        return (method === undefined) ? chain : method;
    };

    var old = $.fn.hierarchySelect;

    $.fn.hierarchySelect = Plugin;
    $.fn.hierarchySelect.defaults = {
        width: 'auto',
        height: '208px',
        hierarchy: true,
        search: true
    };
    $.fn.hierarchySelect.Constructor = HierarchySelect;

    $.fn.hierarchySelect.noConflict = function () {
        $.fn.hierarchySelect = old;
        return this;
    };

    function processElementOffset(parent, element) {
        if (parent.offsetHeight + parent.scrollTop < element.offsetTop + element.offsetHeight) {
            parent.scrollTop = element.offsetTop + element.offsetHeight - parent.offsetHeight;
        } else if (parent.scrollTop > element.offsetTop) {
            parent.scrollTop = element.offsetTop;
        }
    }
    
    function accentsTidy(s){
        var r = s.toLowerCase();
        var non_asciis = {'a': '[àáâãäå]', 'ae': 'æ', 'c': 'ç', 'e': '[èéêë]', 'i': '[ìíîï]', 'n': 'ñ', 'o': '[òóôõö]', 'oe': 'œ', 'u': '[ùúûűü]', 'y': '[ýÿ]'};
        for (i in non_asciis) { r = r.replace(new RegExp(non_asciis[i], 'g'), i); }
        return r;
    };
})(jQuery);