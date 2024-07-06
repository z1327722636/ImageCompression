<!-- components/CompressionResultList.vue -->
<template>
  <div class="compression-result-list">
    <div class="actions">
      <a-radio-group @change="handleSelectChange" v-model="selectedOption">
        <a-radio value="all">全部选中</a-radio>
        <a-radio value="none">全部不选</a-radio>
      </a-radio-group>
    </div>
    <div v-for="(file, index) in files" :key="file.compressedName">
      <CompressionResultItem
        :checked="file.checked"
        :originalName="file.originalName"
        :compressedFileName="file.compressedName"
        :compression="file.algorithm"
        :originalSize="file.originalSize"
        :compressedSize="file.compressedSize"
        :compressionRate="file.compressionRate"
        :thumbnail="file.thumbnail"
        @check-change="updateCheckStatus(index, $event)"
        @download="downloadFile(file)"
      />
    </div>
  </div>
</template>

<script>
import CompressionResultItem from './CompressionResultItem.vue'

export default {
  components: {
    CompressionResultItem
  },
  props: {
    files: Array
  },
  methods: {
    handleSelectChange (e) {
      const value = e.target.value
      this.$emit(value === 'all' ? 'select-all' : 'deselect-all')
    },
    updateCheckStatus (index, checked) {
      this.$emit('update-check-status', index, checked)
    },
    downloadFile (file) {
      this.$emit('download-file', file)
    }
  }
}
</script>

<style scoped>
.compression-result-list {
  margin-top: 20px;
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
}
.actions {
  display: flex;
  margin-bottom: 10px;
}
</style>
