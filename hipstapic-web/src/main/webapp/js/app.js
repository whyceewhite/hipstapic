var hip = hip || {};

hip.app = angular.module('app', 
	[
		'ui.router',
		'ui.bootstrap'
	]
);

hip.app.config(
    ['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/');
            $stateProvider.
                state('error', {
                    url: '/error',
                    templateUrl: 'partials/error.html',
                    controller: 'ErrorController'
                }).
                state('pic-search', {
                    url: '/search?tags&page&pageSize',
                    templateUrl: 'partials/pic-list.html',
                    controller: 'ListController'
                }).
                state('home', {
                    url: '/',
                    templateUrl: 'partials/pic-list.html',
                    controller: 'ListController'
                })
                ;
        }
    ]
);

hip.app.config(['$provide', '$httpProvider',
        function ($provide, $httpProvider) {

            $provide.factory('interceptor', ['$q', '$location', function ($q, $location) {
                return {
                    responseError: function (responseObj) {
                        if (responseObj.status == 403) {
                            window.location = "forbidden.html";
                            return $q.reject(responseObj);
                        }
                        return $q.reject(responseObj);
                    }
                };
            }]);

            $httpProvider.interceptors.push('interceptor');

        }]
);

/**
 * <p>Returns true if the given obj parameter is neither undefined
 * nor null. Returns false if the given obj parameter is undefined
 * or null.</p>
 * 
 * @param   obj {Object} An object or element.
 * @return  true if obj has a value and false if obj is undefined
 *          or null.
 */
hip.isNone = function(obj) {

   return (obj===undefined || obj===null); 
};

/**
 * <p>Returns true if obj is of type string and it has an empty
 * string value. In addition, returns true if obj is undefined or
 * null. Returns false in all other cases.
 * 
 * @param   obj An object or element.
 * @return  true if obj is an empty string or if obj is undefined or
 *          null; false otherwise.
 */
hip.isEmpty = function(obj) {

   if (hip.isNone(obj)) return true;

   if (typeof obj == "string") {
      return $.trim(obj).length == 0;
   }

   return false;
};
/**
* <p>Places an object of criteria into a url to be used for
* search for resource items.</p>
*
* Examples:
* resourceUrl = /app/r/rsrc
* criteria = {} or null or undefined
* return = /app/r/rsrc
*
* criteria = { status : [1, 3], name : "Henry" }
* return = /app/r/rsrc?status=1&status=3&name=Henry
*
* criteria = { status : null }
* return = /app/r/rsrc
*
* @param resourceUrl - {String} The url of the resource for which
* this url is being expanded with search criteria. For
* example: /app-name/r/resource-name. Required.
* @param criteria - {Object} An object containing names and
* values of the criteria to use for searching a resource.
* For example: { status : [1, 3], name : "Henry" }
* @return A url that combines the given resourceUrl and the criteria.
*/
hip.asSearchUri = function(resourceUrl, criteria) {
    var key, value, temp, i,
        hasInitialCriterion = false,
        url = resourceUrl,
        criteria = criteria || {};

    for (key in criteria) {
        temp = "";
        value = criteria[key];
        if (!hip.isNone(value)) {
            if (typeof value === 'object' && value instanceof Array) {
                for (i = 0; i < value.length; i++) {
                    if (i > 0) temp += "&";
                    temp += (key + "=" + encodeURIComponent(value[i]));
                }
            }
            else {
                temp = key + "=" + encodeURIComponent(value);
            }
            if (!hip.isEmpty(temp)) {
                if (!hasInitialCriterion) {
                    url += "?" + temp;
                    hasInitialCriterion = true;
                }
                else {
                    url += "&" + temp;
                }
            }
        }
    }
    return url;
};
