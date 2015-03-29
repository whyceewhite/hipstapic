hip.app.controller('NavController', ['$scope', '$state', 'PictureService', 'ErrorService',
    function ($scope, $state, PictureService, ErrorService) {

        $scope.text = "";

        $scope.search = function () {
            if ($scope.text) {
                $state.go("pic-search", {tags : $scope.text.split(/\s+/)});
            }
        }
    }
]);

hip.app.controller('ListController', ['$scope', '$state', '$stateParams', 'PictureService', 'ErrorService',
    function ($scope, $state, $stateParams, PictureService, ErrorService) {

        $scope.pichost = "http://localhost:9028/images";
        $scope.results = [];
        $scope.resultsCount = 0;

        var pageMinimum = 1,
            page = (hip.isEmpty($stateParams.page)) ? pageMinimum : $stateParams.page,
            pageSize = (hip.isEmpty($stateParams.pageSize)) ? 24 : $stateParams.pageSize,
            tags = (hip.isEmpty($stateParams.tags)) ? [] : $stateParams.tags;

        setSearchParams = function() {
            if (!hip.isEmpty($stateParams.tags)) tags = $stateParams.tags;
            if (!hip.isEmpty($stateParams.page)) page = $stateParams.page;
            if (!hip.isEmpty($stateParams.pageSize)) pageSize = $stateParams.pageSize;
        };

        fetchResultsCount = function() {
            PictureService
                .count({
                    tags : tags
                })
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
                    tags : tags,
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

        setSearchParams();
        fetchResultsCount();
        fetchPage();

    }
]);

hip.app.controller('ErrorController', [ '$scope', 'ErrorService',
    function($scope, ErrorService) {

        $scope.title = ErrorService.getTitle();
        $scope.message = ErrorService.getMessage();
        ErrorService.reset();
    }
]);
