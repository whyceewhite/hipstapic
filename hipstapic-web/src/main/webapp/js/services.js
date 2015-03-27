hip.app.factory('PictureService',
    ['$http', function($http){

        var resourceUrl = 'api/picture';

        return {
            /* Searches for recipes containing the given text.
             *
             * @param   criteria {Object} The criteria for searching. Required.
             */
            search : function(criteria) {
                var url = hip.asSearchUri(resourceUrl + "/search", criteria);
                return $http.get(url);
            },
            /* Searches for recipes containing the given text.
             *
             * @param   criteria {Object} The criteria for searching. Required.
             */
            count : function(criteria) {
                var url = hip.asSearchUri(resourceUrl + "/search/count", criteria);
                return $http.get(url);
            }
        };
    }]
);

hip.app.factory('ErrorService',
    [ function() {

      return {
         title : "",
         message : "",
         getMessage : function() {
            return this.message;
         },
         setMessage : function(message) {
            this.message = message;
         },
         getTitle : function() {
            return this.title;
         },
         setTitle : function(title) {
            this.title = title;
         },
         reset : function() {
            this.title = "";
            this.message = "";
         }
      };

   }]
);