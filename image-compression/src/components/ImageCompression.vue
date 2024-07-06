<template>
<div id="image-compression">
<FileUpload><MyDialog>支持上传照片</MyDialog></FileUpload>
<div id="compression-settings"><CompressionSetting/></div>
<div id="compression-result"><CompressionResultList
      :files="compressFileList"
      @select-all="selectAllFiles"
      @deselect-all="deselectAllFiles"
      @update-check-status="updateFileCheckStatus"
      @download-file="downloadFile"
    /></div>
</div>

</template>

<script>
import CompressionSetting from './CompressionSetting.vue'
import CompressionResultList from './CompressionResultList.vue'
import { mapState, mapActions } from 'vuex'
export default {
  components: {
    CompressionSetting,
    CompressionResultList
  },
  computed: {
    ...mapState(['compressFileList'])
  },
  methods: {
    ...mapActions(['updateCompressFileList']),
    selectAllFiles () {
      const updatedFiles = this.compressFileList.map(file => ({
        ...file,
        checked: true
      }))
      this.updateCompressFileList(updatedFiles)
    },
    deselectAllFiles () {
      const updatedFiles = this.compressFileList.map(file => ({
        ...file,
        checked: false
      }))
      this.updateCompressFileList(updatedFiles)
    },
    updateFileCheckStatus (index, checked) {
      const updatedFiles = [...this.compressFileList]
      updatedFiles[index].checked = checked
      this.updateCompressFileList(updatedFiles)
    },
    downloadFile (file) {
      // 实现下载逻辑
      console.log('Downloading file:', file.originalName)
      // 假设文件存储在服务器的 compressionimage 目录
      const fileUrl = file.thumbnail
      const link = document.createElement('a')
      link.href = fileUrl
      link.setAttribute('download', file.originalName) // 下载文件名
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    }
  }
}
</script>

<style>
#compression-settings {
  margin-top: 10px;
}
</style>
