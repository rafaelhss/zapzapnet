//Define an angular module for our app
var zapzapnet = angular.module('zapzapnet', ["ngRoute", "restangular"]);

//Define Routing for app
//Uri /AddNewOrder -> template add_order.html and Controller AddOrderController
//Uri /ShowOrders -> template show_orders.html and Controller AddOrderController
zapzapnet.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/ShowNetwork/:networkId', {
                templateUrl: 'html/network.html',
                controller: 'ShowNetworkController'
            }).
            otherwise({
                redirectTo: '/AddNewOrder'
            });
    }]);

zapzapnet.controller('ShowNetworkController', function($scope, $routeParams, Restangular) {
    $scope.networkId = $routeParams.networkId;

      Restangular.one('network', $routeParams.networkId)
    //Restangular.oneUrl('network', 'http://localhost:8080/network/' + $routeParams.networkId)
        .get().then(function(network){
            console.log('id:'+ network.sigmagraph);
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