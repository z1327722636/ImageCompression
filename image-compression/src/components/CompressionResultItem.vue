<!-- components/CompressionResultItem.vue -->
<template>
  <div class="compression-result-item">
    <a-checkbox :checked="checked" @change="toggleCheck"></a-checkbox>
    <img :src="thumbnail" alt="Thumbnail" class="thumbnail" v-if="thumbnail">
    <span class="file-name">{{ compressedFileName }}</span>
    <span class="compression">{{compression }}编码</span>
    <span class="original-size"><del>{{ originalSize }}</del></span>
    <span class="compressed-size">{{ compressedSize }}</span>
    <span class="compression-rate">压缩比{{ compressionRate }}</span>
    <a-button type="primary" icon="download" @click="downloadFile">下载</a-button>
  </div>
</template>

<script>
export default {
  props: {
    checked: Boolean,
    originalName: String,
    compressedFileName: String,
    compression: String,
    originalSize: String,
    compressedSize: String,
    compressionRate: String,
    thumbnail: String
  },
  methods: {
    toggleCheck (e) {
      this.$emit('check-change', e.target.checked)
    },
    downloadFile () {
      this.$emit('download', this.originalName)
    }
  }
}
</script>

<style scoped>
.compression-result-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #f0f0f0;
  margin-bottom: 10px;
}
.thumbnail {
  width: 50px;
  height: 50px;
  margin-right: 10px;
  margin-left: 10px;
  border-radius: 4px;
}
.file-name {
  flex-grow: 1;
  margin-right: 10px;
}
.compression,
.compression-rate {
  margin-right: 10px;
}
.original-size{
  font-size:10px;
  margin-right: 0px;}
.compressed-size{

  margin-right: 10px;}
</style>
