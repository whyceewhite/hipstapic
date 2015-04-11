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

        var pageMinimum = 1;

        $scope.pichost = "http://localhost:9028/images";
        $scope.results = [];
        $scope.resultsCount = 0;
        $scope.page = (hip.isEmpty($stateParams.page)) ? pageMinimum : $stateParams.page;
        $scope.pageSize = (hip.isEmpty($stateParams.pageSize)) ? 24 : $stateParams.pageSize;

        // There may be zero or more tags that are present within the $stateParams.
        // If there is one tag then $stateParams.tags will be a string with that
        // value. If there are more than one tag then $stateParams.tag will be an
        // array. The following statements make sure that $scope.tags is an array
        // containing the values from the $stateParams.
        $scope.tags = [];
        if (!hip.isEmpty($stateParams.tags)) {
            if (Array.isArray($stateParams.tags)) {
                $scope.tags = $stateParams.tags;
            } else {
                $scope.tags.push($stateParams.tags);
            }
        }

        fetchResultsCount = function() {
            PictureService
                .count({
                    tags : $scope.tags
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
                    tags : $scope.tags,
                    page : $scope.page,
                    pageSize : $scope.pageSize
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

        $scope.totalPages = function() {
            return Math.ceil($scope.resultsCount/$scope.pageSize);
        };

        $scope.getPageMinCount = function() {
            return ($scope.resultsCount == 0) ? 0 : (($scope.page - 1) * $scope.pageSize) + 1;
        };

        $scope.getPageMaxCount = function() {
            return Math.min($scope.page * $scope.pageSize, $scope.resultsCount);
        };

        $scope.canGoNext = function() {
            return $scope.resultsCount >= ($scope.page * $scope.pageSize) + 1;
        };

        $scope.canGoPrevious = function() {
            return $scope.page > pageMinimum;
        };

        $scope.next = function() {
            if ($scope.canGoNext()) {
                $scope.page++;
                fetchPage();
            }
        };

        $scope.previous = function() {
            if ($scope.canGoPrevious()) {
                $scope.page--;
                fetchPage();
            }
        };

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
