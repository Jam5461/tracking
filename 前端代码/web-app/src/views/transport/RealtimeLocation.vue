<template>
  <div id="mapContainer" style="width: 100%; height: 750px;"></div>
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
        script.src = 'http://api.map.baidu.com/api?v=2.0&ak=Y3v7agPWN7SlyFPW1fJzlgcpnyCSEJzz&callback=onBMapCallback&callback=onBMapCallback';
        script.onerror = reject;
        document.head.appendChild(script);
      });
    },
    initMap() {
      const map = new window.BMap.Map('mapContainer');
      map.enableScrollWheelZoom(true);

      // 使用Geolocation API获取用户当前位置
      const geolocation = new window.BMap.Geolocation();
      geolocation.getCurrentPosition((result) => {
        if (geolocation.getStatus() === window.BMAP_STATUS_SUCCESS) {
          const point = new window.BMap.Point(result.longitude, result.latitude);
          map.centerAndZoom(point, 15);

          // 创建一个标记
          const marker = new window.BMap.Marker(point);
          // 将标记添加到地图上
          map.addOverlay(marker);
        } else {
          alert('定位失败：' + this.getStatus());
        }
      }, {enableHighAccuracy: true});
    },
  },
};
</script>