hip.app.controller('NavController', ['$scope', '$state', 'PictureService', 'ErrorService',
    function ($scope, $state, PictureService, ErrorService) {

        $scope.text = "";

        $scope.search = function () {
            if ($scope.text) {
                $state.go("pic-search", {text : $scope.text});

            }
        }
    }
]);

hip.app.controller('ListController', ['$scope', '$state', 'PictureService', 'ErrorService',
    function ($scope, $state, PictureService, ErrorService) {

        $scope.results = [];
        $scope.resultsCount = 0;

        var pageMinimum = 1,
            page = pageMinimum,
            pageSize = 12;

        fetchResultsCount = function() {
            PictureService
                .count({})
                .success(function (data, status) {
                    $scope.resultsCount = data;
                })
                .error(function (data, status) {
                    ErrorService.setTitle("An error occurred when fetching the pics.");
                    ErrorService.setMessage(data);
                    $state.go("error");
                });
        };

        fetchPage = function() {
            PictureService
                .search({
                    page : page,
                    pageSize : pageSize
                })
                .success(function (data, status) {
                    $scope.results = data;
                })
                .error(function (data, status) {
                    ErrorService.setTitle("An error occurred when fetching the pics.");
                    ErrorService.setMessage(data);
                    $state.go("error");
                });
        };

        $scope.canGoNext = function() {
            return $scope.resultsCount >= (page * pageSize) + 1;
        };

        $scope.canGoPrevious = function() {
            return page > pageMinimum;
        };

        $scope.next = function() {
            if ($scope.canGoNext()) {
                page++;
                fetchPage();
            }
        };

        $scope.previous = function() {
            if ($scope.canGoPrevious()) {
                page--;
                fetchPage();
            }
        };

        fetchResultsCount();
        fetchPage();

    }
]);

hip.app.controller('SearchController', ['$scope', '$stateParams', '$state', 'PictureService', 'ErrorService',
    function ($scope, $stateParams, $state, PictureService, ErrorService) {

        $scope.results = [];

        PictureService
            .search($stateParams)
            .success(function (data, status) {
                $scope.results = data;
            })
            .error(function (data, status) {
                ErrorService.setTitle("An error occurred when searching for pictures.");
                ErrorService.setMessage(data);
                $state.go("error");
            });
    }
]);

hip.app.controller('ErrorController', [ '$scope', 'ErrorService',
    function($scope, ErrorService) {

        $scope.title = ErrorService.getTitle();
        $scope.message = ErrorService.getMessage();
        ErrorService.reset();
    }
]);
