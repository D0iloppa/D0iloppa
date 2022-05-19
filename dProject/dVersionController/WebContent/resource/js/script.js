$(document).ready(function () {
    angular.module('angularResizable', [])
        .directive('resizable', function () {
            var toCall;

            function throttle(fun) {
                if (toCall === undefined) {
                    toCall = fun;
                    setTimeout(function () {
                        toCall();
                        toCall = undefined;
                    }, 100);
                } else {
                    toCall = fun;
                }
            }
            return {
                restrict: 'AE',
                scope: {
                    rDirections: '=',
                    rCenteredX: '=',
                    rCenteredY: '=',
                    rWidth: '=',
                    rHeight: '=',
                    rFlex: '=',
                    rGrabber: '@',
                    rDisabled: '@'
                },
                link: function (scope, element, attr) {
                    var flexBasis = 'flexBasis' in document.documentElement.style ? 'flexBasis' :
                        'webkitFlexBasis' in document.documentElement.style ? 'webkitFlexBasis' :
                        'msFlexPreferredSize' in document.documentElement.style ? 'msFlexPreferredSize' : 'flexBasis';

                    // register watchers on width and height attributes if they are set
                    scope.$watch('rWidth', function (value) {
                        element[0].style.width = scope.rWidth + 'px';
                    });
                    scope.$watch('rHeight', function (value) {
                        element[0].style.height = scope.rHeight + 'px';
                    });

                    element.addClass('resizable');

                    var style = window.getComputedStyle(element[0], null),
                        w,
                        h,
                        dir = scope.rDirections,
                        vx = scope.rCenteredX ? 2 : 1, // if centered double velocity
                        vy = scope.rCenteredY ? 2 : 1, // if centered double velocity
                        inner = scope.rGrabber ? scope.rGrabber : '<span></span>',
                        start,
                        dragDir,
                        axis,
                        info = {};

                    var updateInfo = function (e) {
                        info.width = false;
                        info.height = false;
                        if (axis === 'x')
                            info.width = parseInt(element[0].style[scope.rFlex ? flexBasis : 'width']);
                        else
                            info.height = parseInt(element[0].style[scope.rFlex ? flexBasis : 'height']);
                        info.id = element[0].id;
                        info.evt = e;
                    };

                    var dragging = function (e) {
                        var prop, offset = axis === 'x' ? start - e.clientX : start - e.clientY;
                        switch (dragDir) {
                            case 'top':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h + (offset * vy) + 'px';
                                break;
                            case 'bottom':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h - (offset * vy) + 'px';
                                break;
                            case 'right':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w - (offset * vx) + 'px';
                                break;
                            case 'left':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w + (offset * vx) + 'px';
                                break;
                        }
                        updateInfo(e);
                        throttle(function () {
                            scope.$emit('angular-resizable.resizing', info);
                        });
                    };
                    var dragEnd = function (e) {
                        updateInfo();
                        scope.$emit('angular-resizable.resizeEnd', info);
                        scope.$apply();
                        document.removeEventListener('mouseup', dragEnd, false);
                        document.removeEventListener('mousemove', dragging, false);
                        element.removeClass('no-transition');
                    };
                    var dragStart = function (e, direction) {
                        dragDir = direction;
                        axis = dragDir === 'left' || dragDir === 'right' ? 'x' : 'y';
                        start = axis === 'x' ? e.clientX : e.clientY;
                        w = parseInt(style.getPropertyValue('width'));
                        h = parseInt(style.getPropertyValue('height'));

                        //prevent transition while dragging
                        element.addClass('no-transition');

                        document.addEventListener('mouseup', dragEnd, false);
                        document.addEventListener('mousemove', dragging, false);

                        // Disable highlighting while dragging
                        if (e.stopPropagation) e.stopPropagation();
                        if (e.preventDefault) e.preventDefault();
                        e.cancelBubble = true;
                        e.returnValue = false;

                        updateInfo(e);
                        scope.$emit('angular-resizable.resizeStart', info);
                        scope.$apply();
                    };
					if(dir != null){
						dir.forEach(function (direction) {
							var grabber = document.createElement('div');

							// add class for styling purposes
							grabber.setAttribute('class', 'rg-' + direction);
							grabber.innerHTML = inner;
							element[0].appendChild(grabber);
							grabber.ondragstart = function () {
								return false;
							};
							grabber.addEventListener('mousedown', function (e) {
								var disabled = (scope.rDisabled === 'true');
								if (!disabled && e.which === 1) {
									// left mouse click
									dragStart(e, direction);
								}
							}, false);
                 	   });
					}
                }
            };
        });
    
    angular.module('angularResizable2', [])
        .directive('resizable', function () {
            var toCall;

            function throttle(fun) {
                if (toCall === undefined) {
                    toCall = fun;
                    setTimeout(function () {
                        toCall();
                        toCall = undefined;
                    }, 100);
                } else {
                    toCall = fun;
                }
            }
            return {
                restrict: 'AE',
                scope: {
                    rDirections: '=',
                    rCenteredX: '=',
                    rCenteredY: '=',
                    rWidth: '=',
                    rHeight: '=',
                    rFlex: '=',
                    rGrabber: '@',
                    rDisabled: '@'
                },
                link: function (scope, element, attr) {
                    var flexBasis = 'flexBasis' in document.documentElement.style ? 'flexBasis' :
                        'webkitFlexBasis' in document.documentElement.style ? 'webkitFlexBasis' :
                        'msFlexPreferredSize' in document.documentElement.style ? 'msFlexPreferredSize' : 'flexBasis';

                    // register watchers on width and height attributes if they are set
                    scope.$watch('rWidth', function (value) {
                        element[0].style.width = scope.rWidth + 'px';
                    });
                    scope.$watch('rHeight', function (value) {
                        element[0].style.height = scope.rHeight + 'px';
                    });

                    element.addClass('resizable');

                    var style = window.getComputedStyle(element[0], null),
                        w,
                        h,
                        dir = scope.rDirections,
                        vx = scope.rCenteredX ? 2 : 1, // if centered double velocity
                        vy = scope.rCenteredY ? 2 : 1, // if centered double velocity
                        inner = scope.rGrabber ? scope.rGrabber : '<span></span>',
                        start,
                        dragDir,
                        axis,
                        info = {};

                    var updateInfo = function (e) {
                        info.width = false;
                        info.height = false;
                        if (axis === 'x')
                            info.width = parseInt(element[0].style[scope.rFlex ? flexBasis : 'width']);
                        else
                            info.height = parseInt(element[0].style[scope.rFlex ? flexBasis : 'height']);
                        info.id = element[0].id;
                        info.evt = e;
                    };

                    var dragging = function (e) {
                        var prop, offset = axis === 'x' ? start - e.clientX : start - e.clientY;
                        switch (dragDir) {
                            case 'top':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h + (offset * vy) + 'px';
                                break;
                            case 'bottom':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h - (offset * vy) + 'px';
                                break;
                            case 'right':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w - (offset * vx) + 'px';
                                break;
                            case 'left':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w + (offset * vx) + 'px';
                                break;
                        }
                        updateInfo(e);
                        throttle(function () {
                            scope.$emit('angular-resizable.resizing', info);
                        });
                    };
                    var dragEnd = function (e) {
                        updateInfo();
                        scope.$emit('angular-resizable.resizeEnd', info);
                        scope.$apply();
                        document.removeEventListener('mouseup', dragEnd, false);
                        document.removeEventListener('mousemove', dragging, false);
                        element.removeClass('no-transition');
                    };
                    var dragStart = function (e, direction) {
                        dragDir = direction;
                        axis = dragDir === 'left' || dragDir === 'right' ? 'x' : 'y';
                        start = axis === 'x' ? e.clientX : e.clientY;
                        w = parseInt(style.getPropertyValue('width'));
                        h = parseInt(style.getPropertyValue('height'));

                        //prevent transition while dragging
                        element.addClass('no-transition');

                        document.addEventListener('mouseup', dragEnd, false);
                        document.addEventListener('mousemove', dragging, false);

                        // Disable highlighting while dragging
                        if (e.stopPropagation) e.stopPropagation();
                        if (e.preventDefault) e.preventDefault();
                        e.cancelBubble = true;
                        e.returnValue = false;

                        updateInfo(e);
                        scope.$emit('angular-resizable.resizeStart', info);
                        scope.$apply();
                    };
					if(dir != null){
						dir.forEach(function (direction) {
							var grabber = document.createElement('div');

							// add class for styling purposes
							grabber.setAttribute('class', 'rg-' + direction);
							grabber.innerHTML = inner;
							element[0].appendChild(grabber);
							grabber.ondragstart = function () {
								return false;
							};
							grabber.addEventListener('mousedown', function (e) {
								var disabled = (scope.rDisabled === 'true');
								if (!disabled && e.which === 1) {
									// left mouse click
									dragStart(e, direction);
								}
							}, false);
						});
					}
                }
            };
        });
	
	angular.module('angularResizable3', [])
        .directive('resizable', function () {
            var toCall;

            function throttle(fun) {
                if (toCall === undefined) {
                    toCall = fun;
                    setTimeout(function () {
                        toCall();
                        toCall = undefined;
                    }, 100);
                } else {
                    toCall = fun;
                }
            }
            return {
                restrict: 'AE',
                scope: {
                    rDirections: '=',
                    rCenteredX: '=',
                    rCenteredY: '=',
                    rWidth: '=',
                    rHeight: '=',
                    rFlex: '=',
                    rGrabber: '@',
                    rDisabled: '@'
                },
                link: function (scope, element, attr) {
                    var flexBasis = 'flexBasis' in document.documentElement.style ? 'flexBasis' :
                        'webkitFlexBasis' in document.documentElement.style ? 'webkitFlexBasis' :
                        'msFlexPreferredSize' in document.documentElement.style ? 'msFlexPreferredSize' : 'flexBasis';

                    // register watchers on width and height attributes if they are set
                    scope.$watch('rWidth', function (value) {
                        element[0].style.width = scope.rWidth + 'px';
                    });
                    scope.$watch('rHeight', function (value) {
                        element[0].style.height = scope.rHeight + 'px';
                    });

                    element.addClass('resizable');

                    var style = window.getComputedStyle(element[0], null),
                        w,
                        h,
                        dir = scope.rDirections,
                        vx = scope.rCenteredX ? 2 : 1, // if centered double velocity
                        vy = scope.rCenteredY ? 2 : 1, // if centered double velocity
                        inner = scope.rGrabber ? scope.rGrabber : '<span></span>',
                        start,
                        dragDir,
                        axis,
                        info = {};

                    var updateInfo = function (e) {
                        info.width = false;
                        info.height = false;
                        if (axis === 'x')
                            info.width = parseInt(element[0].style[scope.rFlex ? flexBasis : 'width']);
                        else
                            info.height = parseInt(element[0].style[scope.rFlex ? flexBasis : 'height']);
                        info.id = element[0].id;
                        info.evt = e;
                    };

                    var dragging = function (e) {
                        var prop, offset = axis === 'x' ? start - e.clientX : start - e.clientY;
                        switch (dragDir) {
                            case 'top':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h + (offset * vy) + 'px';
                                break;
                            case 'bottom':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h - (offset * vy) + 'px';
                                break;
                            case 'right':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w - (offset * vx) + 'px';
                                break;
                            case 'left':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w + (offset * vx) + 'px';
                                break;
                        }
                        updateInfo(e);
                        throttle(function () {
                            scope.$emit('angular-resizable.resizing', info);
                        });
                    };
                    var dragEnd = function (e) {
                        updateInfo();
                        scope.$emit('angular-resizable.resizeEnd', info);
                        scope.$apply();
                        document.removeEventListener('mouseup', dragEnd, false);
                        document.removeEventListener('mousemove', dragging, false);
                        element.removeClass('no-transition');
                    };
                    var dragStart = function (e, direction) {
                        dragDir = direction;
                        axis = dragDir === 'left' || dragDir === 'right' ? 'x' : 'y';
                        start = axis === 'x' ? e.clientX : e.clientY;
                        w = parseInt(style.getPropertyValue('width'));
                        h = parseInt(style.getPropertyValue('height'));

                        //prevent transition while dragging
                        element.addClass('no-transition');

                        document.addEventListener('mouseup', dragEnd, false);
                        document.addEventListener('mousemove', dragging, false);

                        // Disable highlighting while dragging
                        if (e.stopPropagation) e.stopPropagation();
                        if (e.preventDefault) e.preventDefault();
                        e.cancelBubble = true;
                        e.returnValue = false;

                        updateInfo(e);
                        scope.$emit('angular-resizable.resizeStart', info);
                        scope.$apply();
                    };
					if(dir != null){
						dir.forEach(function (direction) {
							var grabber = document.createElement('div');

							// add class for styling purposes
							grabber.setAttribute('class', 'rg-' + direction);
							grabber.innerHTML = inner;
							element[0].appendChild(grabber);
							grabber.ondragstart = function () {
								return false;
							};
							grabber.addEventListener('mousedown', function (e) {
								var disabled = (scope.rDisabled === 'true');
								if (!disabled && e.which === 1) {
									// left mouse click
									dragStart(e, direction);
								}
							}, false);
						});
					}
                }
            };
        });
	
	angular.module('angularResizable4', [])
        .directive('resizable', function () {
            var toCall;

            function throttle(fun) {
                if (toCall === undefined) {
                    toCall = fun;
                    setTimeout(function () {
                        toCall();
                        toCall = undefined;
                    }, 100);
                } else {
                    toCall = fun;
                }
            }
            return {
                restrict: 'AE',
                scope: {
                    rDirections: '=',
                    rCenteredX: '=',
                    rCenteredY: '=',
                    rWidth: '=',
                    rHeight: '=',
                    rFlex: '=',
                    rGrabber: '@',
                    rDisabled: '@'
                },
                link: function (scope, element, attr) {
                    var flexBasis = 'flexBasis' in document.documentElement.style ? 'flexBasis' :
                        'webkitFlexBasis' in document.documentElement.style ? 'webkitFlexBasis' :
                        'msFlexPreferredSize' in document.documentElement.style ? 'msFlexPreferredSize' : 'flexBasis';

                    // register watchers on width and height attributes if they are set
                    scope.$watch('rWidth', function (value) {
                        element[0].style.width = scope.rWidth + 'px';
                    });
                    scope.$watch('rHeight', function (value) {
                        element[0].style.height = scope.rHeight + 'px';
                    });

                    element.addClass('resizable');

                    var style = window.getComputedStyle(element[0], null),
                        w,
                        h,
                        dir = scope.rDirections,
                        vx = scope.rCenteredX ? 2 : 1, // if centered double velocity
                        vy = scope.rCenteredY ? 2 : 1, // if centered double velocity
                        inner = scope.rGrabber ? scope.rGrabber : '<span></span>',
                        start,
                        dragDir,
                        axis,
                        info = {};

                    var updateInfo = function (e) {
                        info.width = false;
                        info.height = false;
                        if (axis === 'x')
                            info.width = parseInt(element[0].style[scope.rFlex ? flexBasis : 'width']);
                        else
                            info.height = parseInt(element[0].style[scope.rFlex ? flexBasis : 'height']);
                        info.id = element[0].id;
                        info.evt = e;
                    };

                    var dragging = function (e) {
                        var prop, offset = axis === 'x' ? start - e.clientX : start - e.clientY;
                        switch (dragDir) {
                            case 'top':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h + (offset * vy) + 'px';
                                break;
                            case 'bottom':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h - (offset * vy) + 'px';
                                break;
                            case 'right':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w - (offset * vx) + 'px';
                                break;
                            case 'left':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w + (offset * vx) + 'px';
                                break;
                        }
                        updateInfo(e);
                        throttle(function () {
                            scope.$emit('angular-resizable.resizing', info);
                        });
                    };
                    var dragEnd = function (e) {
                        updateInfo();
                        scope.$emit('angular-resizable.resizeEnd', info);
                        scope.$apply();
                        document.removeEventListener('mouseup', dragEnd, false);
                        document.removeEventListener('mousemove', dragging, false);
                        element.removeClass('no-transition');
                    };
                    var dragStart = function (e, direction) {
                        dragDir = direction;
                        axis = dragDir === 'left' || dragDir === 'right' ? 'x' : 'y';
                        start = axis === 'x' ? e.clientX : e.clientY;
                        w = parseInt(style.getPropertyValue('width'));
                        h = parseInt(style.getPropertyValue('height'));

                        //prevent transition while dragging
                        element.addClass('no-transition');

                        document.addEventListener('mouseup', dragEnd, false);
                        document.addEventListener('mousemove', dragging, false);

                        // Disable highlighting while dragging
                        if (e.stopPropagation) e.stopPropagation();
                        if (e.preventDefault) e.preventDefault();
                        e.cancelBubble = true;
                        e.returnValue = false;

                        updateInfo(e);
                        scope.$emit('angular-resizable.resizeStart', info);
                        scope.$apply();
                    };
					if(dir != null){
						dir.forEach(function (direction) {
							var grabber = document.createElement('div');

							// add class for styling purposes
							grabber.setAttribute('class', 'rg-' + direction);
							grabber.innerHTML = inner;
							element[0].appendChild(grabber);
							grabber.ondragstart = function () {
								return false;
							};
							grabber.addEventListener('mousedown', function (e) {
								var disabled = (scope.rDisabled === 'true');
								if (!disabled && e.which === 1) {
									// left mouse click
									dragStart(e, direction);
								}
							}, false);
						});
					}
                }
            };
        });
	
	angular.module('angularResizable5', [])
        .directive('resizable', function () {
            var toCall;

            function throttle(fun) {
                if (toCall === undefined) {
                    toCall = fun;
                    setTimeout(function () {
                        toCall();
                        toCall = undefined;
                    }, 100);
                } else {
                    toCall = fun;
                }
            }
            return {
                restrict: 'AE',
                scope: {
                    rDirections: '=',
                    rCenteredX: '=',
                    rCenteredY: '=',
                    rWidth: '=',
                    rHeight: '=',
                    rFlex: '=',
                    rGrabber: '@',
                    rDisabled: '@'
                },
                link: function (scope, element, attr) {
                    var flexBasis = 'flexBasis' in document.documentElement.style ? 'flexBasis' :
                        'webkitFlexBasis' in document.documentElement.style ? 'webkitFlexBasis' :
                        'msFlexPreferredSize' in document.documentElement.style ? 'msFlexPreferredSize' : 'flexBasis';

                    // register watchers on width and height attributes if they are set
                    scope.$watch('rWidth', function (value) {
                        element[0].style.width = scope.rWidth + 'px';
                    });
                    scope.$watch('rHeight', function (value) {
                        element[0].style.height = scope.rHeight + 'px';
                    });

                    element.addClass('resizable');

                    var style = window.getComputedStyle(element[0], null),
                        w,
                        h,
                        dir = scope.rDirections,
                        vx = scope.rCenteredX ? 2 : 1, // if centered double velocity
                        vy = scope.rCenteredY ? 2 : 1, // if centered double velocity
                        inner = scope.rGrabber ? scope.rGrabber : '<span></span>',
                        start,
                        dragDir,
                        axis,
                        info = {};

                    var updateInfo = function (e) {
                        info.width = false;
                        info.height = false;
                        if (axis === 'x')
                            info.width = parseInt(element[0].style[scope.rFlex ? flexBasis : 'width']);
                        else
                            info.height = parseInt(element[0].style[scope.rFlex ? flexBasis : 'height']);
                        info.id = element[0].id;
                        info.evt = e;
                    };

                    var dragging = function (e) {
                        var prop, offset = axis === 'x' ? start - e.clientX : start - e.clientY;
                        switch (dragDir) {
                            case 'top':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h + (offset * vy) + 'px';
                                break;
                            case 'bottom':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h - (offset * vy) + 'px';
                                break;
                            case 'right':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w - (offset * vx) + 'px';
                                break;
                            case 'left':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w + (offset * vx) + 'px';
                                break;
                        }
                        updateInfo(e);
                        throttle(function () {
                            scope.$emit('angular-resizable.resizing', info);
                        });
                    };
                    var dragEnd = function (e) {
                        updateInfo();
                        scope.$emit('angular-resizable.resizeEnd', info);
                        scope.$apply();
                        document.removeEventListener('mouseup', dragEnd, false);
                        document.removeEventListener('mousemove', dragging, false);
                        element.removeClass('no-transition');
                    };
                    var dragStart = function (e, direction) {
                        dragDir = direction;
                        axis = dragDir === 'left' || dragDir === 'right' ? 'x' : 'y';
                        start = axis === 'x' ? e.clientX : e.clientY;
                        w = parseInt(style.getPropertyValue('width'));
                        h = parseInt(style.getPropertyValue('height'));

                        //prevent transition while dragging
                        element.addClass('no-transition');

                        document.addEventListener('mouseup', dragEnd, false);
                        document.addEventListener('mousemove', dragging, false);

                        // Disable highlighting while dragging
                        if (e.stopPropagation) e.stopPropagation();
                        if (e.preventDefault) e.preventDefault();
                        e.cancelBubble = true;
                        e.returnValue = false;

                        updateInfo(e);
                        scope.$emit('angular-resizable.resizeStart', info);
                        scope.$apply();
                    };
					if(dir != null){
						dir.forEach(function (direction) {
							var grabber = document.createElement('div');

							// add class for styling purposes
							grabber.setAttribute('class', 'rg-' + direction);
							grabber.innerHTML = inner;
							element[0].appendChild(grabber);
							grabber.ondragstart = function () {
								return false;
							};
							grabber.addEventListener('mousedown', function (e) {
								var disabled = (scope.rDisabled === 'true');
								if (!disabled && e.which === 1) {
									// left mouse click
									dragStart(e, direction);
								}
							}, false);
						});
					}
                }
            };
        });
    
    angular.module('angularResizable6', [])
        .directive('resizable', function () {
            var toCall;

            function throttle(fun) {
                if (toCall === undefined) {
                    toCall = fun;
                    setTimeout(function () {
                        toCall();
                        toCall = undefined;
                    }, 100);
                } else {
                    toCall = fun;
                }
            }
            return {
                restrict: 'AE',
                scope: {
                    rDirections: '=',
                    rCenteredX: '=',
                    rCenteredY: '=',
                    rWidth: '=',
                    rHeight: '=',
                    rFlex: '=',
                    rGrabber: '@',
                    rDisabled: '@'
                },
                link: function (scope, element, attr) {
                    var flexBasis = 'flexBasis' in document.documentElement.style ? 'flexBasis' :
                        'webkitFlexBasis' in document.documentElement.style ? 'webkitFlexBasis' :
                        'msFlexPreferredSize' in document.documentElement.style ? 'msFlexPreferredSize' : 'flexBasis';

                    // register watchers on width and height attributes if they are set
                    scope.$watch('rWidth', function (value) {
                        element[0].style.width = scope.rWidth + 'px';
                    });
                    scope.$watch('rHeight', function (value) {
                        element[0].style.height = scope.rHeight + 'px';
                    });

                    element.addClass('resizable');

                    var style = window.getComputedStyle(element[0], null),
                        w,
                        h,
                        dir = scope.rDirections,
                        vx = scope.rCenteredX ? 2 : 1, // if centered double velocity
                        vy = scope.rCenteredY ? 2 : 1, // if centered double velocity
                        inner = scope.rGrabber ? scope.rGrabber : '<span></span>',
                        start,
                        dragDir,
                        axis,
                        info = {};

                    var updateInfo = function (e) {
                        info.width = false;
                        info.height = false;
                        if (axis === 'x')
                            info.width = parseInt(element[0].style[scope.rFlex ? flexBasis : 'width']);
                        else
                            info.height = parseInt(element[0].style[scope.rFlex ? flexBasis : 'height']);
                        info.id = element[0].id;
                        info.evt = e;
                    };

                    var dragging = function (e) {
                        var prop, offset = axis === 'x' ? start - e.clientX : start - e.clientY;
                        switch (dragDir) {
                            case 'top':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h + (offset * vy) + 'px';
                                break;
                            case 'bottom':
                                prop = scope.rFlex ? flexBasis : 'height';
                                element[0].style[prop] = h - (offset * vy) + 'px';
                                break;
                            case 'right':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w - (offset * vx) + 'px';
                                break;
                            case 'left':
                                prop = scope.rFlex ? flexBasis : 'width';
                                element[0].style[prop] = w + (offset * vx) + 'px';
                                break;
                        }
                        updateInfo(e);
                        throttle(function () {
                            scope.$emit('angular-resizable.resizing', info);
                        });
                    };
                    var dragEnd = function (e) {
                        updateInfo();
                        scope.$emit('angular-resizable.resizeEnd', info);
                        scope.$apply();
                        document.removeEventListener('mouseup', dragEnd, false);
                        document.removeEventListener('mousemove', dragging, false);
                        element.removeClass('no-transition');
                    };
                    var dragStart = function (e, direction) {
                        dragDir = direction;
                        axis = dragDir === 'left' || dragDir === 'right' ? 'x' : 'y';
                        start = axis === 'x' ? e.clientX : e.clientY;
                        w = parseInt(style.getPropertyValue('width'));
                        h = parseInt(style.getPropertyValue('height'));

                        //prevent transition while dragging
                        element.addClass('no-transition');

                        document.addEventListener('mouseup', dragEnd, false);
                        document.addEventListener('mousemove', dragging, false);

                        // Disable highlighting while dragging
                        if (e.stopPropagation) e.stopPropagation();
                        if (e.preventDefault) e.preventDefault();
                        e.cancelBubble = true;
                        e.returnValue = false;

                        updateInfo(e);
                        scope.$emit('angular-resizable.resizeStart', info);
                        scope.$apply();
                    };
					if(dir != null){
						dir.forEach(function (direction) {
							var grabber = document.createElement('div');

							// add class for styling purposes
							grabber.setAttribute('class', 'rg-' + direction);
							grabber.innerHTML = inner;
							element[0].appendChild(grabber);
							grabber.ondragstart = function () {
								return false;
							};
							grabber.addEventListener('mousedown', function (e) {
								var disabled = (scope.rDisabled === 'true');
								if (!disabled && e.which === 1) {
									// left mouse click
									dragStart(e, direction);
								}
							}, false);
						});
					}
                }
            };
        });
    
    

    $(document).on("click", ".tab_wrap .tab_tit li a", function () {
        var loca = $(this).attr("href");
        $(".tab_wrap .tab_tit li").removeClass("on");
        $(this).closest("li").addClass("on");
        $(".tab_wrap .tab_cont > div").removeClass("on");
        $(loca).addClass("on");
        return false;
    });

    $(document).on("click", ".id_tab .id_tab_tit li a", function () {
        var loca = $(this).attr("href");
        $(".id_tab .id_tab_tit li a").removeClass("on");
        $(this).addClass("on");
        $(".id_tab .id_tab_cont .box").removeClass("on");
        $(loca).addClass("on");
        return false;
    });


    $(document).on("click", ".pr_tab_tit li a", function () {
        var loca = $(this).attr("href");
        $(".pr_tab_tit li a").removeClass("on");
        $(this).addClass("on");
        $(".pr_tab_cont .pr_box").removeClass("on");
        $(loca).addClass("on");
        return false;
    });


    $(document).on("click", ".as_tab_tit li a", function () {
        var loca = $(this).attr("href");
        $(".as_tab_tit li a").removeClass("on");
        $(this).addClass("on");
        $(".as_tab_cont .as_box").removeClass("on");
        $(loca).addClass("on");
        return false;
    });

//    $(document).on("click", ".proj_general ul li a", function () {
//        var loca = $(this).attr("href");
//        $(".proj_general ul li a").removeClass("on");
//        $(".pg_cont .pg").removeClass("on");
//        $(this).addClass("on");
//        $(loca).addClass("on");
//        return false;
//    });
    $(document).on("click", "header .top .btn_menu", function () {

        if (!$("body").hasClass("open")) {
            $("body").addClass("open");
            $(".lnb").addClass("on");
        } else {
            $("body").removeClass("open");
        }
    });
    $("html").click(function (e) {
        if (!$(e.target).closest(".lnb").length) {
            $("body").removeClass("open");
            $(".lnb").removeClass("on");
        }
    });
    if ($(this).width() < 960) {
        $("body").removeClass("open");
        $(".lnb").removeClass("on");
    }
    $(window).resize(function () {
        if ($(this).width() < 960) {
            $("body").removeClass("open");
            $(".lnb").removeClass("on");
        }
    });

    $(document).on("click", ".btn_share", function () {
        $("+ .share_box", this).toggleClass("on");
        return false;
    });

    $(document).on("click", ".multi_sel", function () {
        $(this).closest(".multi_sel_wrap").addClass("on");
        return false;
    });
    $(document).on("click", ".multi_box ul li a", function () {
        $(this).closest("ul").find("li a").removeClass("on");
        $(this).toggleClass("on");
        return false;
    });
    $("html").click(function (e) {
        if (!$(e.target).closest(".share_box").length) {
            $(".share_box").removeClass("on");
        }

        if (!$(e.target).closest(".multi_sel_wrap").length) {
            $(".multi_sel_wrap").removeClass("on");
        }
    });
    $(document).on("click", ".contents aside .menu2nd > li > a", function () {
        $(".contents aside .menu2nd > li > a").removeClass("on");
        $(this).addClass("on");
        $("+.menu3rd", this).addClass("on");
        $("+.menu3rd .menu4th", this).addClass("on");
        /*$("+ .menu3rd", this).addClass("on");*/
    });

    $(document).on("click", ".contents aside .menu2nd > li > .menu3rd > li > a", function () {
        $(".contents aside .menu2nd > li > .menu3rd > li > a").removeClass("on");
        $(this).addClass("on");
        $(".menu4th").addClass("on");
        /*$("+ .menu4th", this).addClass("on");*/
    });
    $(document).on("click", ".popup .pop_cont .btn_close_pop", function () {
        $(this).closest(".popup").removeClass("on");
        return false;
    });
    $(document).on("click", ".popup .pop_top .btn_close", function () {
        $(this).closest(".popup").removeClass("on");
        return false;
    });
    $(".faq:not(.faq_edit) dl dd").hide();
    $(".faq:not(.faq_edit) dl dt").click(function () {
        $(".faq:not(.faq_edit) dl dt").removeClass("on");
        $(this).addClass("on");
        $(".faq:not(.faq_edit) dl dd").stop().slideUp();
        $("+ dd", this).stop().slideDown();
        return false;
    });

    $(document).on("click", ".btn_top", function () {
        $("header .hd_form").toggleClass("on");
        return false;
    });
    $(".date").datepicker({ dateFormat: 'yy-mm-dd' });

    $(document).on('change', '.pr_gn', function () {
        $(".sel_tab > div").removeClass("on");
        var loca = $(this).val();
        $(loca).addClass("on");
    });
    
    
    $(document).on("click", ".acco h4", function(){
        $(this).addClass("on");
        $("+ .tbl_wrap", this).stop().slideDown();
        return false;
    });


    $(document).on("click", ".acco h4.on", function(){
        $(this).removeClass("on");
        $("+ .tbl_wrap", this).stop().slideUp();
        return false;

    });
    
    /*
    $(document).on("click", ".acco h4", function () {
       $(".acco").removeClass("on");
        $(this).closest(".acco").addClass("on");
		$(".acco .tbl_wrap").stop().slideUp();
        $(".acco.on .tbl_wrap").stop().slideDown();/*$(".sch_opt .tbl_wrap").slideUp();
        $("+ .tbl_wrap", this).slideDown();
        return false;
    });
	*/

    var fileTarget = $('.filebox .upload-hidden');

    fileTarget.on('change', function () {
        if (window.FileReader) {
            var filename = $(this)[0].files[0].name;
        } else {
            var filename = $(this).val().split('/').pop().split('\\').pop();
        }

        $(this).siblings('.upload-name').val(filename);
        $(this).parent().siblings('#rfile_download').html(filename);
        $(this).parent().siblings('#rfile_download').addClass('property_attach');
        $(this).parent().siblings('#rfile_download').removeClass('property_download');
        $(this).parent().siblings('#tr_incoming_uploadFileInfo').html(filename);
        $(this).parent().siblings('#tr_incoming_uploadFileInfo').addClass('property_attach');
        $(this).parent().siblings('#tr_incoming_uploadFileInfo').removeClass('property_download');
    });

});
