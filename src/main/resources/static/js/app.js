var zapzapnet = angular.module('zapzapnet', ["ngRoute", "restangular"]);

zapzapnet.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/ShowNetwork/:networkId', {
                templateUrl: 'html/network2.html',
                controller: 'ShowNetworkController'
            }).
            otherwise({
                redirectTo: '/AddNewOrder'
            });
    }]);

zapzapnet.controller('ShowNetworkController', function($scope, $routeParams, Restangular) {

    $scope.colors = ["#800026", "#bd0026", "#e31a1c", "#fc4e2a", "#fd8d3c", "#feb24c", "#fed976"];


    console.log($routeParams.networkId)
    $scope.networkId = $routeParams.networkId;

    Restangular.one('network', $routeParams.networkId)
        //  Restangular.oneUrl('network', 'http://localhost:8080/network/' + $routeParams.networkId)
        .get().then(function(network){
            console.log('id:'+ network.sigmagraph);


                  console.log(network.messageMetrics.wordCloud);

              $scope.words = network.messageMetrics.wordCloud;


              $scope.network = network;


            /**
             * This example shows how to use the dragNodes plugin.
             */
            var i,
                s,
                N = network.nodes.length,
                E = network.edges.length,
                g = {
                    nodes: [],
                    edges: []
                };

            // Generate a random graph:
            for (i = 0; i < N; i++)
                g.nodes.push({
                    id: network.nodes[i],
                    label: network.nodes[i],
                    x: Math.random(),
                    y: Math.random(),
                    size: 1,
                    color: '#666'
                });

            for (i = 0; i < E; i++)
                g.edges.push({
                    id: 'e' + i,
                    source: network.edges[i].source,
                    target: network.edges[i].target,
                    size: 1,
                    color: '#ccc'
                });



                // sigma.renderers.def = sigma.renderers.canvas
                // Instantiate sigma:
                s = new sigma({
                    graph: g,
                    container: 'graph-container'
                });

                // Initialize the dragNodes plugin:
                var dragListener = sigma.plugins.dragNodes(s, s.renderers[0]);

                dragListener.bind('startdrag', function(event) {
                    console.log(event);
                });
                dragListener.bind('drag', function(event) {
                    console.log(event);
                });
                dragListener.bind('drop', function(event) {
                    console.log(event);
                });
                dragListener.bind('dragend', function(event) {
                    console.log(event);
                });

        });
});


zapzapnet.directive('jqcloud', ['$parse', function($parse) {
    // get existing options
    var defaults = jQuery.fn.jQCloud.defaults.get(),
        jqcOptions = [];

    for (var opt in defaults) {
        if (defaults.hasOwnProperty(opt)) {
            jqcOptions.push(opt);
        }
    }

    return {
        restrict: 'E',
        template: '<div></div>',
        replace: true,
        scope: {
            words: '=words',
            afterCloudRender: '&'
        },
        link: function($scope, $elem, $attr) {
            var options = {};

            for (var i=0, l=jqcOptions.length; i<l; i++) {
                var opt = jqcOptions[i];
                var attr = $attr[opt] || $elem.attr(opt);
                if (attr !== undefined) {
                    options[opt] = $parse(attr)();
                }
            }

            if ($scope.afterCloudRender) {
                options.afterCloudRender = $scope.afterCloudRender;
            }

            jQuery($elem).jQCloud($scope.words, options);

            $scope.$watchCollection('words', function() {
                $scope.$evalAsync(function() {
                    var words = [];
                    $.extend(words,$scope.words);
                    jQuery($elem).jQCloud('update', words);
                });
            });

            $elem.bind('$destroy', function() {
                jQuery($elem).jQCloud('destroy');
            });
        }
    };
}]);
