<template>
    <div id="mapContainer" style="width: 100%; height: 400px;"></div>
  </template>
  
  <script>
  export default {
    name: 'BaiduMap',
    mounted() {
      this.loadBaiduMapScript().then(() => {
        this.initMap();
      });
    },
    methods: {
      loadBaiduMapScript() {
        return new Promise((resolve, reject) => {
          if (window.BMap) {
            resolve(window.BMap);
            return true;
          }
          window.onBMapCallback = function () {
            resolve(window.BMap);
          };
          const script = document.createElement('script');
          script.type = 'text/javascript';
          script.src = 'http://api.map.baidu.com/api?v=2.0&ak=Y3v7agPWN7SlyFPW1fJzlgcpnyCSEJzz&callback=onBMapCallback';
          script.onerror = reject;
          document.head.appendChild(script);
        });
      },
      initMap() {
        const map = new window.BMap.Map('mapContainer');
        const point = new window.BMap.Point(116.404, 39.915);
        map.centerAndZoom(point, 15);
      },
    },
  };
  </script>