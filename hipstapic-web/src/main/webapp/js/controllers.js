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

        var pageMinimum = 1,
            pagingPeekCount = 5,
            pagingPeekHalfBefore = Math.floor((pagingPeekCount - 1) / 2),
            pagingPeekHalfAfter = Math.ceil((pagingPeekCount - 1) / 2);


        $scope.pichost = "http://localhost:9028/images";
        $scope.results = [];
        $scope.resultsCount = 0;
        $scope.page = (hip.isEmpty($stateParams.page) || $stateParams.page < 1) ? pageMinimum : $stateParams.page;
        $scope.pageSize = (hip.isEmpty($stateParams.pageSize)) ? 12 : $stateParams.pageSize;
        $scope.pageRange = [];
        $scope.totalPages = 0;
        /*
                $scope.resultsCount = data;
                $scope.totalPages = Math.ceil($scope.resultsCount/$scope.pageSize);
                $scope.pageMinCount = ($scope.resultsCount == 0) ? 0 : (($scope.page - 1) * $scope.pageSize) + 1;
                $scope.pageMaxCount = Math.min($scope.page * $scope.pageSize, $scope.resultsCount);
                $scope.isOnFirstPage = $scope.page == 1;
                $scope.isOnLastPage = $scope.page == $scope.totalPages;
                $scope.canGoNext = $scope.resultsCount >= ($scope.page * $scope.pageSize) + 1;
                $scope.canGoPrevious = $scope.page > pageMinimum;
                $scope.nextPage = $scope.canGoNext ? $scope.page + 1 : $scope.page;
                $scope.previousPage = $scope.canGoPrevious ? $scope.page - 1 : $scope.page;
        */

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

        calculateRange = function() {

            var peekRangeMin = Math.max($scope.page - pagingPeekHalfBefore, 1),
                peekRangeMax = Math.min($scope.page + pagingPeekHalfAfter, $scope.totalPages),
                unusedPeek = pagingPeekCount - (peekRangeMax - peekRangeMin + 1),
                rangeObj = [];

            if (unusedPeek > 0) {
                if (peekRangeMin != 1) {
                    peekRangeMin = Math.max(peekRangeMin - unusedPeek, 1);
                } else if (peekRangeMax != $scope.totalPages) {
                    peekRangeMax = Math.min(peekRangeMax + unusedPeek, $scope.totalPages);
                }
            }

            for (var i = peekRangeMin; i <= peekRangeMax; i++) {
                rangeObj.push({
                    pageNumber : i,
                    isCurrentPage : i == $scope.page
                });
            }

            return rangeObj;
        };

        fetchResultsCount = function() {
            PictureService
                .count({
                    tags : $scope.tags
                })
                .success(function (data, status) {
                    $scope.resultsCount = data;
                    $scope.totalPages = Math.ceil($scope.resultsCount/$scope.pageSize);
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
                    $scope.pageRange = calculateRange();
                })
                .error(function (data, status) {
                    ErrorService.setTitle("An error occurred when fetching the pics.");
                    ErrorService.setMessage(data);
                    $state.go("error");
                });
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

        $scope.getNextPageNumber = function() {
            return $scope.canGoNext() ? $scope.page + 1 : $scope.page;
        };

        $scope.getPreviousPageNumber = function() {
            return $scope.canGoPrevious() ? $scope.page - 1 : $scope.page;
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

        $scope.first = function() {
            if ($scope.page > 1) {
                $scope.page = 1;
                fetchPage();
            }
        };

        $scope.last = function() {
            if ($scope.page < $scope.totalPages) {
                $scope.page = $scope.totalPages;
                fetchPage();
            }
        };

        $scope.gotoPage = function(pageNumber) {
            if (pageNumber &&
                    pageNumber != $scope.page &&
                    pageNumber >= 1 &&
                    pageNumber <= $scope.totalPages) {
                $scope.page = pageNumber;
                fetchPage();
            }
        };

        $scope.isOnFirstPage = function() {
            return $scope.page == 1;
        };

        $scope.isOnLastPage = function() {
            return $scope.page == $scope.totalPages();
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
