/**
 * @description
 * A component that captures when the Enter key up action is applied on the
 * associated element.
 *
 * @usage
 * <input wh-enterup="doSomething()">
 */
hip.app.directive('whEnterup', function() {
    return function(scope, element, attrs) {
        element.bind('keyup', function(event) {
            if (event.which == '13') {
                scope.$apply(attrs.whEnterup);
            }
        });
    };
});

/**
 * @description
 * A component that captures when the key up action on a set of given keys
 * is applied on the associated element.
 *
 * @usage
 * <input wh-keyup="doSomething()" keys="[27,13]">
 */
hip.app.directive('whKeyup', function() {
    return function(scope, element, attrs) {

        function applyKeyup() {
           scope.$apply(attrs.whKeyup);
        };

        var allowedKeys = scope.$eval(attrs.keys);
        element.bind('keyup', function(event) {
            if (!allowedKeys || allowedKeys.length == 0) {
                applyKeyup();
            } else {
                angular.forEach(allowedKeys, function(key) {
                    if (key == event.which) {
                        applyKeyup();
                    }
                });
            }
        });
    };
});
