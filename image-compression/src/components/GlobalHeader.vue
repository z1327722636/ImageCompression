<template>
  <div id="global-header">
    <a-menu v-model="current" mode="horizontal" class="header-menu">
      <a-menu-item
  key="0"

  disabled
>
  <div class="title-bar">
    <img class="logo" src="../assets/logo.png" />
    <div class="title">compression</div>
  </div>
</a-menu-item>
      <a-menu-item key="picture"> <a-icon type="picture" />图片压缩 </a-menu-item>
      <a-menu-item key="app" disabled> <a-icon type="appstore" />其他功能敬请期待 </a-menu-item>
      <!-- <a-sub-menu>
        <span slot="title" class="submenu-title-wrapper"
          ><a-icon type="setting" />Navigation Three - Submenu</span
        >
        <a-menu-item-group title="Item 1">
          <a-menu-item key="setting:1">
            Option 1
          </a-menu-item>
          <a-menu-item key="setting:2">
            Option 2
          </a-menu-item>
        </a-menu-item-group>
        <a-menu-item-group title="Item 2">
          <a-menu-item key="setting:3">
            Option 3
          </a-menu-item>
          <a-menu-item key="setting:4">
            Option 4
          </a-menu-item>
        </a-menu-item-group>
      </a-sub-menu>
      <a-menu-item key="alipay">
        <a href="https://antdv.com" target="_blank" rel="noopener noreferrer"
          >Navigation Four - Link</a
        >
      </a-menu-item> -->
    </a-menu>
  </div>
</template>
<script>
export default {
  data () {
    return {
      current: ['mail']
    }
  }
}
</script>

<style  scoped>
#global-header .header-menu{
  align-items: center;
  line-height: 64px;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .15);
}

.a-menu-item{
  text-align: center;
}
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  margin-left: 16px;
}

.logo {
  height: 50px;
}
</style>
