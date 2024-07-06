<template>
  <div id="basic-layout">
     <a-layout>
      <a-layout-header><GlobalHeader /></a-layout-header>
      <a-layout-content><ImageCompression/></a-layout-content>
      <a-layout-footer>本项目由zaki制作</a-layout-footer>
    </a-layout>

  </div>
</template>

<script>
import GlobalHeader from '../components/GlobalHeader.vue'
import ImageCompression from '../components/ImageCompression.vue'
export default {
  components: {
    GlobalHeader,
    ImageCompression
  }
}
</script>

<style scoped >
#basic-layout {
  height: 100vh;
}

.ant-layout-header {
  margin-bottom: 16px;
  background: white;
  box-shadow: #eee 1px 1px 5px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  padding: 0 ;
  z-index: 999;
}

.ant-layout-content{
  margin-top: 64px;
  padding: 20px;
  background: linear-gradient(to right, #fefefe, #fff);
}

.ant-layout {
  width: 100%;
  min-height: 100%;
}

.ant-layout-footer {
background-color: #efefef;
padding: 16px;
width: 100%;
text-align:center;
}
</style>
